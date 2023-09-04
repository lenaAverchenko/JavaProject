package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.ManagerRepository;
import telran.functionality.com.repository.ProductRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ManagerRepository managerRepository;

    private List<Manager> managers;

    private List<Product> products;

    @BeforeEach
    public void init(){
        managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        products = Arrays.asList(
                new Product(1, managers.get(1), "credit", Currency.PLN, 7.00, 100000, new Timestamp(new Date().getTime())),
                new Product(2, managers.get(1), "mortgage", Currency.PLN, 3.00, 500000, new Timestamp(new Date().getTime())),
                new Product(3, managers.get(1), "classic", Currency.PLN, 1.00, 10000, new Timestamp(new Date().getTime())),
                new Product(4, managers.get(2), "credit", Currency.UAH, 15.00, 500000, new Timestamp(new Date().getTime())),
                new Product(5, managers.get(2), "mortgage", Currency.UAH, 20.00, 1000000, new Timestamp(new Date().getTime())),
                new Product(6, managers.get(2), "classic", Currency.UAH, 1.00, 50000, new Timestamp(new Date().getTime())));
    }


    @Test
    void getAll() {
        Mockito.when(productRepository.findAll()).thenReturn(products);
        assertEquals(6, productService.getAll().size());
    }

    @Test
    void getAllNotExisting() {
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> productService.getAll());
    }

    @Test
    void getById() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        Product product = productService.getById(1);
        assertEquals("credit", product.getName());
        assertEquals(Currency.PLN, product.getCurrencyCode());
        assertEquals(7.00, product.getInterestRate());
        assertEquals(100000, product.getLimitValue());
    }

    @Test
    void getByIdNotExisting() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> productService.getById(1));
    }

    @Test
    void save() {
        Product newProduct = new Product(managers.get(1), "new", Currency.PLN, 10.00, 5000, new Timestamp(new Date().getTime()));
        Mockito.when(productRepository.save(newProduct)).thenReturn(newProduct);
        Product savedProduct = productService.save(newProduct);
        assertEquals("new", savedProduct.getName());
        assertEquals(Currency.PLN, savedProduct.getCurrencyCode());
        assertEquals(10.00, savedProduct.getInterestRate());
        assertEquals(5000, savedProduct.getLimitValue());
    }

    @Test
    void changeManager() {
        Mockito.when(productRepository.findById(4L)).thenReturn(Optional.ofNullable(products.get(3)));
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(0)));
        productService.changeManager(4, 1);
        Product changedProduct = productService.getById(4);
        assertEquals(4, changedProduct.getId());
        assertEquals(1, changedProduct.getManager().getId());
    }

    @Test
    void changeStatus() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        productService.changeStatus(1, Status.WAITING);
        assertEquals(Status.WAITING, productService.getById(1).getStatus());
    }

    @Test
    void changeLimitValue() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        productService.changeLimitValue(1, 5000);
        assertEquals(5000, productService.getById(1).getLimitValue());

    }

    @Test
    void delete() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        Product product = productService.getById(1);
        productService.delete(product.getId());
        Mockito.verify(productRepository).delete(product);
    }

    @Test
    void inactivateStatus() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        productService.inactivateStatus(1);
        assertEquals(Status.INACTIVE, productService.getById(1).getStatus());
    }

    @Test
    void productExists() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        boolean answer = productService.productExists(productService.getById(1L));
        assertTrue(answer);
    }

    @Test
    void productExistsWithException() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> productService.productExists(productService.getById(1L)));
    }

    @Test
    void statusIsValid() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products.get(0)));
        boolean answer = productService.statusIsValid(1);
        assertTrue(answer);
    }
}