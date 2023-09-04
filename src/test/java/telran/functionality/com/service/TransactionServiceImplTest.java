package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.TransactionRepository;


import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    private List<Transaction> transactions;

    private List<Account> accounts;

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


        transactions = Arrays.asList(
                new Transaction(UUID.randomUUID(), accounts.get(1), accounts.get(2), Type.PERSONAL, 100, "Personal transfer"),
                new Transaction(UUID.randomUUID(), accounts.get(2), accounts.get(0), Type.COMMERCIAL, 1000, "Payment for service"),
                new Transaction(UUID.randomUUID(), accounts.get(0), accounts.get(2), Type.COMMERCIAL, 5000, "Salary payment"));
    }

    @Test
    void getAll() {
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);
        assertEquals(3,transactionService.getAll().size());
    }

    @Test
    void getAllNotExist() {
        Mockito.when(transactionRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> transactionService.getAll());
    }


    @Test
    void getById() {
        UUID transactionId = transactions.get(0).getId();
        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(transactions.get(0)));
        Transaction transaction = transactionService.getById(transactionId);
        assertEquals(accounts.get(1).getId(), transaction.getDebitAccount().getId());
        assertEquals(accounts.get(2).getId(), transaction.getCreditAccount().getId());
        assertEquals(Type.PERSONAL, transaction.getType());
        assertEquals(100, transaction.getAmount());
        assertEquals("Personal transfer", transaction.getDescription());
    }

    @Test
    void getByIdNotExisting() {
        UUID someId = UUID.randomUUID();
        Mockito.when(transactionRepository.findById(someId)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> transactionService.getById(someId));
    }
    @Test
    void save() {
          Transaction newTransaction = new Transaction(
                  UUID.randomUUID(), accounts.get(2), accounts.get(1),
                  Type.COMMERCIAL, 800, "New personal transfer");
        Mockito.when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);
        Transaction savedTransaction = transactionService.save(newTransaction);
        assertEquals(accounts.get(2).getId(), savedTransaction.getDebitAccount().getId());
        assertEquals(accounts.get(1).getId(), savedTransaction.getCreditAccount().getId());
        assertEquals(Type.COMMERCIAL, savedTransaction.getType());
        assertEquals(800, savedTransaction.getAmount());
        assertEquals("New personal transfer", savedTransaction.getDescription());
    }

    @Test
    void delete() {
        UUID transactionId = transactions.get(1).getId();
        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(transactions.get(1)));
        Transaction transaction = transactionService.getById(transactionId);
        transactionService.delete(transactionId);
        Mockito.verify(transactionRepository).delete(transaction);
    }
}