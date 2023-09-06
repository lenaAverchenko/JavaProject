package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import telran.functionality.com.converter.CurrencyConverter;
import telran.functionality.com.dto.BalanceDto;
import telran.functionality.com.entity.*;

import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.AccountRepository;
import telran.functionality.com.repository.AgreementRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private CurrencyConverter converter;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private TransactionService transactionService;

    private List<Account> accounts;
    private List<Client> clients;
    private List<Manager> managers;
    private List<Transaction> transactions;


    @BeforeEach
    public void init(){
        managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        clients = Arrays.asList(
                new Client(UUID.randomUUID(), managers.get(0), new ArrayList<>(), Status.ACTIVE, "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(),Status.ACTIVE, "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(2), new ArrayList<>(),Status.ACTIVE, "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));

        accounts = Arrays.asList(
                new Account(UUID.randomUUID(),clients.get(0), "Bank Account", Type.INNERBANK, Status.ACTIVE, 0, Currency.PLN, new Timestamp(new Date().getTime())),
                new Account(UUID.randomUUID(),clients.get(1), "Personal Account", Type.PERSONAL, Status.ACTIVE, 1000, Currency.PLN, new Timestamp(new Date().getTime())),
                new Account(UUID.randomUUID(),clients.get(2), "Leo's Account", Type.PERSONAL, Status.ACTIVE, 200, Currency.PLN, new Timestamp(new Date().getTime())));
        transactions = Arrays.asList(
                new Transaction(UUID.randomUUID(), accounts.get(1), accounts.get(2), Type.PERSONAL, 100, "Personal transfer"),
                new Transaction(UUID.randomUUID(), accounts.get(2), accounts.get(0), Type.COMMERCIAL, 1000, "Payment for service"),
                new Transaction(UUID.randomUUID(), accounts.get(0), accounts.get(2), Type.COMMERCIAL, 5000, "Salary payment"));

    }



    @Test
    void getAll() {
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        assertEquals(3, accountService.getAll().size());
    }

    @Test
    void getAllNotExisting() {
        Mockito.when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> accountService.getAll());
    }

    @Test
    void getById() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Account account = accountService.getById(accountId);
        assertEquals(accountId, account.getId());
        assertEquals(clients.get(0).getId(), account.getClient().getId());
        assertEquals(Type.INNERBANK, account.getType());
        assertEquals(Status.ACTIVE, account.getStatus());
        assertEquals("Bank Account", account.getName());
        assertEquals(Currency.PLN, account.getCurrencyCode());
    }

    @Test
    void getByIdNotExisting() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> accountService.getById(accountId));
    }

    @Test
    void save() {
        Account account = new Account(UUID.randomUUID(),clients.get(1), "Personal Account", Type.PERSONAL, Status.ACTIVE, 1000, Currency.PLN, new Timestamp(new Date().getTime()));
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Account savedAccount = accountService.save(account);
        assertEquals(account.getId(), savedAccount.getId());
        assertEquals(clients.get(1).getId(), savedAccount.getClient().getId());
        assertEquals(Type.PERSONAL, savedAccount.getType());
        assertEquals(Status.ACTIVE, savedAccount.getStatus());
        assertEquals("Personal Account", savedAccount.getName());
        assertEquals(Currency.PLN, savedAccount.getCurrencyCode());
    }

    @Test
    void changeStatusWithNoAgreement() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        assertThrows(AccountIsNotValidException.class, () -> accountService.changeStatus(accountId, Status.WAITING));
    }

    @Test
    void changeStatus() {
        Account accountInit = accounts.get(0);
        UUID accountId = accountInit.getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountInit));
        Agreement agreement = new Agreement(1, accounts.get(0),
                new Product(5, managers.get(2), "mortgage", Currency.PLN, 20.00,
                        1000000, new Timestamp(new Date().getTime())),
                3.00, Status.ACTIVE, 100000, new Timestamp(new Date().getTime()));
        accountInit.setAgreement(agreement);
        Account account = accountService.getById(accountInit.getId());
//        Mockito.when(agreementRepository.findAll()).thenReturn(Arrays.asList(agreement));
        accountService.changeStatus(accountId, Status.WAITING);
        assertEquals(Status.WAITING, account.getStatus());
    }



    @Test
    void getBalanceOf() {
        UUID accountId = accounts.get(0).getId();
        Account account = accounts.get(0);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(account));
        BalanceDto balanceDto = accountService.getBalanceOf(accountId);
        assertEquals(accountId, balanceDto.getIdOfAccount());
        assertEquals(0, balanceDto.getBalance());
        assertEquals(Currency.PLN, balanceDto.getCurrencyCode());
     }


    @Test
    void getHistoryOfTransactionsByAccountId() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Mockito.when(transactionService.getAll()).thenReturn(transactions);
        List<Transaction> foundTransactions = accountService.getHistoryOfTransactionsByAccountId(accountId);
        assertEquals(2, foundTransactions.size());
    }

    @Test
    void transferMoneyBetweenAccountsSumNegative() {
        Transaction transaction = new Transaction(UUID.randomUUID(), accounts.get(2), accounts.get(0), Type.COMMERCIAL, -1000, "Payment for service");
        assertThrows(WrongValueException.class, () -> accountService.transferMoneyBetweenAccounts(transaction));
    }

    @Test
    void transferMoneyBetweenAccountsInactiveDebitAccount() {
        Account account =  new Account(UUID.randomUUID(),clients.get(0), "Bank Account", Type.INNERBANK, Status.INACTIVE, 0, Currency.PLN, new Timestamp(new Date().getTime()));
        Transaction transaction = new Transaction(UUID.randomUUID(), account, accounts.get(0), Type.COMMERCIAL, 100, "Payment for service");
        Account debitAccount = account;
        Mockito.when(accountRepository.findById(accounts.get(0).getId())).thenReturn(Optional.ofNullable(debitAccount));
        Account creditAccount = accountService.getById(accounts.get(0).getId());
        Mockito.when(accountRepository.findById(debitAccount.getId())).thenReturn(Optional.ofNullable(debitAccount));
        Mockito.when(accountRepository.findById(creditAccount.getId())).thenReturn(Optional.ofNullable(creditAccount));
        assertThrows(InvalidStatusException.class, () -> accountService.transferMoneyBetweenAccounts(transaction));
    }

    @Test
    void transferMoneyBetweenAccountsInactiveCreditAccount() {
        Account account =  new Account(UUID.randomUUID(),clients.get(0), "Bank Account", Type.INNERBANK, Status.INACTIVE, 0, Currency.PLN, new Timestamp(new Date().getTime()));
        Transaction transaction = new Transaction(UUID.randomUUID(), accounts.get(2), account, Type.COMMERCIAL, 100, "Payment for service");
        Mockito.when(accountRepository.findById(accounts.get(2).getId())).thenReturn(Optional.ofNullable(accounts.get(2)));
        Account debitAccount = accountService.getById(accounts.get(2).getId());
        Account creditAccount = account;
        Mockito.when(accountRepository.findById(debitAccount.getId())).thenReturn(Optional.ofNullable(debitAccount));
        Mockito.when(accountRepository.findById(creditAccount.getId())).thenReturn(Optional.ofNullable(creditAccount));
        assertThrows(InvalidStatusException.class, () -> accountService.transferMoneyBetweenAccounts(transaction));
    }
    @Test
    void transferMoneyBetweenAccounts() {
        Mockito.when(accountRepository.findById(accounts.get(2).getId())).thenReturn(Optional.ofNullable(accounts.get(2)));
        Mockito.when(accountRepository.findById(accounts.get(0).getId())).thenReturn(Optional.ofNullable(accounts.get(0)));
        Account debitAccount = accountService.getById(accounts.get(2).getId());
        Account creditAccount = accountService.getById(accounts.get(0).getId());
        Transaction transaction = new Transaction(UUID.randomUUID(), debitAccount, creditAccount, Type.COMMERCIAL, 150, "Payment for service");
        Transaction createdTransaction = accountService.transferMoneyBetweenAccounts(transaction);
        assertEquals(50, createdTransaction.getDebitAccount().getBalance());
        assertEquals(150, createdTransaction.getCreditAccount().getBalance());
        assertEquals(150, createdTransaction.getAmount());

    }

    void transferMoneyBetweenAccountsNotEnoughMoney() {
        Transaction transaction = new Transaction(UUID.randomUUID(), accounts.get(2), accounts.get(0), Type.COMMERCIAL, 1000, "Payment for service");
        Account debitAccount = accountService.getById(accounts.get(2).getId());
        Account creditAccount = accountService.getById(accounts.get(0).getId());
        Mockito.when(accountRepository.findById(debitAccount.getId())).thenReturn(Optional.ofNullable(debitAccount));
        Mockito.when(accountRepository.findById(creditAccount.getId())).thenReturn(Optional.ofNullable(creditAccount));
        assertThrows(NotEnoughMoneyException.class, () -> accountService.transferMoneyBetweenAccounts(transaction));
    }

    @Test
    void accountBelongsToClient() {
        UUID accountId = accounts.get(0).getId();
        Account account = accounts.get(0);
        UUID clientId = account.getClient().getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(account));
        assertTrue(accountService.accountBelongsToClient(clientId,accountId));
    }

    @Test
    void accountDoesNotBelongsToClient() {
        UUID accountId = accounts.get(0).getId();
        Account account = accounts.get(0);
        UUID clientId = accounts.get(2).getClient().getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(account));
        assertThrows(AccountDoesntBelongToClientException.class, () -> accountService.accountBelongsToClient(clientId,accountId));
    }

    @Test
    void withdrawMoney() {
        Mockito.when(accountRepository.findById(accounts.get(2).getId())).thenReturn(Optional.ofNullable(accounts.get(2)));
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        Account account = accountService.getById(accounts.get(2).getId());
        Account newAccount = accountService.withdrawMoney(account.getClient().getId(), account.getId(),150);
        assertEquals(50, newAccount.getBalance());
    }

    @Test
    void withdrawTooMuchMoney() {
        Mockito.when(accountRepository.findById(accounts.get(2).getId())).thenReturn(Optional.ofNullable(accounts.get(2)));
//        Mockito.when(accountRepository.findById(accounts.get(0).getId())).thenReturn(Optional.ofNullable(accounts.get(0)));
        Account account = accountService.getById(accounts.get(2).getId());
        assertThrows(NotEnoughMoneyException.class, () -> accountService.withdrawMoney(account.getClient().getId(), account.getId(),1000));
    }


    @Test
    void putMoney() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        Account account = accountService.putMoney(accountId, 1000);
        assertEquals(accountId, account.getId());
        assertEquals(1000, account.getBalance());
    }

    @Test
    void inactivateAccount() {
        Account accountInit = accounts.get(0);
        UUID accountId = accountInit.getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountInit));
        Agreement agreement = new Agreement(1, accounts.get(0),
                new Product(5, managers.get(2), "mortgage", Currency.PLN, 20.00,
                        1000000, new Timestamp(new Date().getTime())),
                3.00, Status.ACTIVE, 100000, new Timestamp(new Date().getTime()));
        accountInit.setAgreement(agreement);
        Account account = accountService.getById(accountInit.getId());
        accountService.inactivateAccount(accountId);
        assertEquals(Status.INACTIVE, accountService.getById(accountId).getStatus());
    }

    @Test
    void accountExists() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        boolean answer = accountService.accountExists(accountService.getById(accountId));
        assertTrue(answer);
    }

    @Test
    void accountIsValid() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        boolean answer = accountService.accountIsValid(accountId);
        assertTrue(answer);
    }

    @Test
    void needsToBeConverted() {
        boolean answer = accountService.needsToBeConverted(Currency.PLN, Currency.UAH);
        assertTrue(answer);
    }
    @Test
    void doesNotNeedToBeConverted() {
        boolean answer = accountService.needsToBeConverted(Currency.PLN, Currency.PLN);
        assertFalse(answer);
    }
}
