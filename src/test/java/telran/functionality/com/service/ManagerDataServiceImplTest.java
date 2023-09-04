package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.w3c.dom.stylesheets.LinkStyle;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.exceptions.ForbiddenLoginNameException;
import telran.functionality.com.repository.ManagerDataRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManagerDataServiceImplTest {

    @InjectMocks
    private ManagerDataServiceImpl managerDataService;

    @Mock
    private ManagerDataRepository managerDataRepository;

    private List<ManagerData> managerDataList;

    private List<Manager> managers;

    @BeforeEach
    public void init(){
        managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        managerDataList = Arrays.asList(
                new ManagerData(1, "One", "One", managers.get(0)),
                new ManagerData(2, "Two", "Two", managers.get(1)),
                new ManagerData(3, "Three", "Three", managers.get(2)));
    }
    @Test
    void create() {
        ManagerData managerData = new ManagerData(4, "Four", "Four", managers.get(0));
        Mockito.when(managerDataRepository.save(managerData)).thenReturn(managerData);
        ManagerData managerDataCreated = managerDataService.create(managerData);
        assertEquals(4,managerDataCreated.getId());
        assertEquals("Four",managerDataCreated.getLogin());
        assertEquals("Four",managerDataCreated.getPassword());
    }

    @Test
    void getByLogin() {
        Mockito.when(managerDataRepository.findByLogin("One")).thenReturn(managerDataList.get(0));
        ManagerData managerData = managerDataService.getByLogin("One");
        assertEquals(1,managerData.getId());
        assertEquals("One",managerData.getLogin());
        assertEquals("One",managerData.getPassword());
    }

    @Test
    void getByLoginNotExistingManager() {
        Mockito.when(managerDataRepository.findByLogin("One")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> managerDataService.getByLogin("One"));
    }
}