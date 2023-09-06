package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.enums.Status;
import telran.functionality.com.repository.ClientDataRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ClientDataServiceImplTest {

    @InjectMocks
    private ClientDataServiceImpl clientDataService;
    @Mock
    private ClientDataRepository clientDataRepository;

    private List<ClientData> clientDataList;
    private List<Client> clients;



    @BeforeEach
    public void init(){
        List<Manager> managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        clients = Arrays.asList(
                new Client(UUID.randomUUID(), managers.get(0), new ArrayList<>(), Status.ACTIVE, "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(),Status.ACTIVE, "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(2), new ArrayList<>(),Status.ACTIVE, "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));

        clientDataList = Arrays.asList(
                new ClientData(1, "One", "One", clients.get(0)),
                new ClientData(2, "Two", "Two", clients.get(1)),
                new ClientData(3, "Three", "Three", clients.get(2)));
    }

    @Test
    void create() {
        ClientData clientData = new ClientData(4, "Four", "Four", clients.get(0));
        Mockito.when(clientDataRepository.save(clientData)).thenReturn(clientData);
        ClientData clientDataCreated = clientDataService.create(clientData);
        assertEquals(4,clientDataCreated.getId());
        assertEquals("Four",clientDataCreated.getLogin());
        assertEquals("Four",clientDataCreated.getPassword());
    }

    @Test
    void getByLogin() {
        Mockito.when(clientDataRepository.findByLogin("One")).thenReturn(clientDataList.get(0));
        ClientData clientData = clientDataService.getByLogin("One");
        assertEquals(1,clientData.getId());
        assertEquals("One",clientData.getLogin());
        assertEquals("One",clientData.getPassword());
    }

    @Test
//    @Disabled
    void getByLoginNotExistingClient() {
        Mockito.when(clientDataRepository.findByLogin("One")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> clientDataService.getByLogin("One"));
    }
}