package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.enums.Status;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @InjectMocks
    private UserDetailService userDetailService;

    @Mock
    private ClientDataServiceImpl clientService;

    @Mock
    private ManagerDataServiceImpl managerService;

    private List<ClientData> clientDataList;
    private List<ManagerData> managerDataList;

    @BeforeEach
    void init(){
        List<Manager> managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        List<Client> clients = Arrays.asList(
                new Client(UUID.randomUUID(), managers.get(0), new ArrayList<>(), Status.ACTIVE, "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(),Status.ACTIVE, "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(2), new ArrayList<>(),Status.ACTIVE, "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));

        clientDataList = Arrays.asList(
                new ClientData(1, "One", "One", clients.get(0)),
                new ClientData(2, "Two", "Two", clients.get(1)),
                new ClientData(3, "Three", "Three", clients.get(1)));

        managerDataList = Arrays.asList(
                new ManagerData(1, "First", "First", managers.get(0)),
                new ManagerData(2, "Second", "Second", managers.get(1)),
                new ManagerData(3, "Third", "Third", managers.get(1)));
    }

    @Test
    void loadUserByUsernameForClient() {
        String login = "One";
        ClientData client = clientDataList.get(0);
        Mockito.when(clientService.getByLogin(login)).thenReturn(clientDataList.get(0));
        Mockito.when(managerService.getByLogin(login)).thenReturn(null);
        User expectedUser = new User(client.getLogin(), client.getPassword(),
            Arrays.asList(new SimpleGrantedAuthority("USER")));
        assertEquals(expectedUser, userDetailService.loadUserByUsername(login));
    }

    @Test
    void loadUserByUsernameForManager() {
        String login = "First";
        ManagerData manager = managerDataList.get(0);
        Mockito.when(clientService.getByLogin(login)).thenReturn(null);
        Mockito.when(managerService.getByLogin(login)).thenReturn(managerDataList.get(0));
        User expectedUser = new User(manager.getLogin(), manager.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        assertEquals(expectedUser, userDetailService.loadUserByUsername(login));
    }

    @Test
    public void loadUserByUsernameForNoOne() {
        String login = "Some";
        ManagerData manager = managerDataList.get(0);
        Mockito.when(clientService.getByLogin(login)).thenReturn(null);
        Mockito.when(managerService.getByLogin(login)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(login));
    }
}