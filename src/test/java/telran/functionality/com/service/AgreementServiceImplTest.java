package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.AgreementRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AgreementServiceImplTest {

    @InjectMocks
    private AgreementServiceImpl agreementService;
    @Mock
    private AgreementRepository agreementRepository;
    @Mock
    private AccountService accountService;
    private List<Agreement> agreements;
    private List<Account> accounts;
    private List<Product> products;
    @BeforeEach
    public void init(){
        List<Manager> managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        List<Client> clients = Arrays.asList(
                new Client(UUID.randomUUID(), managers.get(0), new ArrayList<>(), Status.ACTIVE, "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(),Status.ACTIVE, "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(2), new ArrayList<>(),Status.ACTIVE, "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));

        accounts = Arrays.asList(
                new Account(UUID.randomUUID(),clients.get(0), "Bank Account", Type.INNERBANK, Status.ACTIVE, 0, Currency.PLN, new Timestamp(new Date().getTime())),
                new Account(UUID.randomUUID(),clients.get(1), "Personal Account", Type.PERSONAL, Status.ACTIVE, 1000, Currency.PLN, new Timestamp(new Date().getTime())),
                new Account(UUID.randomUUID(),clients.get(2), "Leo's Account", Type.PERSONAL, Status.ACTIVE, 200, Currency.USD, new Timestamp(new Date().getTime())));
        products = Arrays.asList(
                new Product(1, managers.get(1), "credit", Currency.PLN, 7.00, 100000, new Timestamp(new Date().getTime())),
                new Product(2, managers.get(1), "mortgage", Currency.PLN, 3.00, 500000, new Timestamp(new Date().getTime())),
                new Product(3, managers.get(1), "classic", Currency.PLN, 1.00, 10000, new Timestamp(new Date().getTime())),
                new Product(4, managers.get(2), "credit", Currency.UAH, 15.00, 500000, new Timestamp(new Date().getTime())),
                new Product(5, managers.get(2), "mortgage", Currency.UAH, 20.00, 1000000, new Timestamp(new Date().getTime())),
                new Product(6, managers.get(2), "classic", Currency.UAH, 1.00, 50000, new Timestamp(new Date().getTime())));
        agreements = Arrays.asList(
                new Agreement(1, accounts.get(0), products.get(0), 3.00, Status.ACTIVE, 100000, new Timestamp(new Date().getTime())),
                new Agreement(2, accounts.get(1), products.get(1), 15.00, Status.ACTIVE, 500000, new Timestamp(new Date().getTime())),
                new Agreement(3, accounts.get(2), products.get(2), 5.00, Status.ACTIVE, 50000, new Timestamp(new Date().getTime())));
    }


    @Test
    void getAll() {
        Mockito.when(agreementRepository.findAll()).thenReturn(agreements);
        assertEquals(3, agreementService.getAll().size());
    }

    @Test
    void getAllNotExisting() {
        Mockito.when(agreementRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> agreementService.getAll());
    }

    @Test
    void getById() {
        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreements.get(0)));
        Agreement agreement = agreementService.getById(1);
        assertEquals(1, agreement.getId());
        assertEquals(Status.ACTIVE, agreement.getStatus());
        assertEquals(3.00, agreement.getInterestRate());
        assertEquals(100000, agreement.getSum());
        assertEquals(1, agreement.getProduct().getId());
        assertEquals(accounts.get(0).getId(), agreement.getAccount().getId());
    }

    @Test
    void getByIdNotExisting() {
        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> agreementService.getById(1));
    }

    @Test
    void save() {
        Agreement agreement = new Agreement(4, accounts.get(0), products.get(0), 10.00, Status.ACTIVE, 200, new Timestamp(new Date().getTime()));
        Mockito.when(agreementRepository.save(agreement)).thenReturn(agreement);
        Agreement savedAgreement = agreementService.save(agreement);
        assertEquals(4, savedAgreement.getId());
        assertEquals(1, savedAgreement.getProduct().getId());
        assertEquals(accounts.get(0).getId(), savedAgreement.getAccount().getId());
        assertEquals(10.00, savedAgreement.getInterestRate());
        assertEquals(Status.ACTIVE, savedAgreement.getStatus());
        assertEquals(200, savedAgreement.getSum());

    }

//    @Test
//    void changeStatus() {
//        Agreement agreementToChange = agreements.get(0);
//        agreementToChange.setAccount(accounts.get(0));
//        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreementToChange));
//        Agreement agreement = agreementService.changeStatus(1, Status.WAITING);
//        assertEquals(Status.WAITING, agreement.getStatus());
//    }

//    @Test
//    void changeInterestRate() {
//        Agreement agreementToChange = agreements.get(0);
//        agreementToChange.setAccount(accounts.get(0));
//        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreementToChange));
//        Agreement agreement = agreementService.changeInterestRate(1, 50);
//        assertEquals(50, agreement.getInterestRate());
//    }

    @Test
    void delete() {
        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreements.get(0)));
        Agreement agreement = agreementService.getById(1);
        agreementService.delete(agreement.getId());
        Mockito.verify(agreementRepository).delete(agreement);
    }

//    @Test
//    void inactivateAgreement() {
//        Agreement agreementToChange = agreements.get(0);
//        agreementToChange.setAccount(accounts.get(1));
//        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreementToChange));
//        agreementService.inactivateAgreement(1);
//        assertEquals(Status.INACTIVE, agreementService.getById(1).getStatus());
//    }

    @Test
    void agreementExists() {
        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreements.get(0)));
        Agreement agreement = agreementService.getById(1);
        boolean answer = agreementService.agreementExists(agreement);
        assertTrue(answer);
    }

    @Test
    void statusIsValid() {
        Mockito.when(agreementRepository.findById(1L)).thenReturn(Optional.ofNullable(agreements.get(0)));
        boolean answer = agreementService.statusIsValid(1);
        assertTrue(answer);
    }
}