package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @InjectMocks
    private ManagerServiceImpl managerService;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private ProductServiceImpl productService;
    @Mock
    private ClientServiceImpl clientService;

    private List<Manager> managers;
    private Product product;

    @BeforeEach
    public void init() {
        managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
        List<Product> products = Arrays.asList(
                new Product(7, managers.get(0), "classic", Currency.PLN, 1.00, 10000, new Timestamp(new Date().getTime())),
                new Product(4, managers.get(0), "classic", Currency.PLN, 1.00, 3000, new Timestamp(new Date().getTime()))
        );
        managers.get(2).setProducts(products);
    }

    @Test
    void getAll() {
        Mockito.when(managerRepository.findAll()).thenReturn(managers);
        assertEquals(3, managerService.getAll().size());
    }

    @Test
    void getAllNotExisting() {
        Mockito.when(managerRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> managerService.getAll());
    }

    @Test
    void getById() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        Manager manager = managerService.getById(1);
        assertEquals("Oleh", manager.getFirstName());
        assertEquals("Olehov", manager.getLastName());
        assertEquals(0, manager.getClients().size());
        assertEquals(0, manager.getProducts().size());
        assertEquals(managers.get(0).getCreatedAt(), manager.getCreatedAt());
    }

    @Test
    void getByIdNotExisting() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> managerService.getById(1));
    }

    @Test
    void save() {
        Manager newManager = new Manager(4, "OlehNew", "OlehovNew", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime()));
        Mockito.when(managerRepository.save(newManager)).thenReturn(newManager);
        Manager savedManager = managerService.save(newManager);
        assertEquals(4, savedManager.getId());
        assertEquals("OlehNew", savedManager.getFirstName());
        assertEquals("OlehovNew", savedManager.getLastName());
        assertEquals(0, savedManager.getClients().size());
        assertEquals(0, savedManager.getProducts().size());
    }


    @Test
    void update() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        Manager manager = new Manager("UpdatedOlehNew", "UpdatedOlehovNew", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime()));
        Manager updatedManager = managerService.update(1, manager);
        assertEquals(1, updatedManager.getId());
        assertEquals("UpdatedOlehNew", updatedManager.getFirstName());
        assertEquals("UpdatedOlehovNew", updatedManager.getLastName());
        assertEquals(0, updatedManager.getClients().size());
        assertEquals(0, updatedManager.getProducts().size());
    }

    @Test
    void delete() {
        Mockito.when(managerRepository.findById(2L)).thenReturn(Optional.ofNullable(managers.get(1)));
        Manager manager = managerService.getById(2);
        managerService.delete(manager.getId());
        Mockito.verify(managerRepository).delete(manager);
    }

    @Test
    void changeStatus() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        managerService.changeStatus(1, Status.WAITING);
        assertEquals(Status.WAITING, managerService.getById(1).getStatus());
    }

    @Test
    void addProduct() {
        Product product = new Product(3, managers.get(0), "classic", Currency.PLN, 1.00, 10000, new Timestamp(new Date().getTime()));
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        Manager manager = managerService.addProduct(1, product);
        Mockito.verify(productService).save(product);
    }

    @Test
    void changeManagerOfProduct() {
        managerService.changeManagerOfProduct(7, 1);
        Mockito.verify(productService).changeManager(7, 1);
    }

    @Test
    void testChangeStatusOfProduct() {
        managerService.changeStatusOfProduct(7, Status.WAITING);
        Mockito.verify(productService).changeStatus(7, Status.WAITING);
    }

    @Test
    void changeLimitValueOfProduct() {
        managerService.changeLimitValueOfProduct(7, 3000);
        Mockito.verify(productService).changeLimitValue(7, 3000);
    }

    @Test
    void inactivateStatus() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        managerService.inactivateStatus(1);
        assertEquals(Status.INACTIVE, managerService.getById(1).getStatus());
    }

    @Test
    void managerExists() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        Manager manager = managerService.getById(1);
        boolean answer = managerService.managerExists(manager);
        assertTrue(answer);
    }

    @Test
    void managerNotExists() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> managerService.getById(1));
    }

    @Test
    void statusIsValid() {
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        boolean answer = managerService.statusIsValid(1);
        assertTrue(answer);
    }

    @Test
    void inactivateStatusOfProduct() {
        managerService.inactivateStatusOfProduct(7);
        Mockito.verify(productService).inactivateStatus(7);
    }
}