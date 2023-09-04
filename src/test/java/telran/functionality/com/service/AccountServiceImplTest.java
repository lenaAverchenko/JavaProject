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
import telran.functionality.com.exceptions.AccountDoesntBelongToClientException;
import telran.functionality.com.exceptions.AccountIsNotValidException;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
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
                new Account(UUID.randomUUID(),clients.get(2), "Leo's Account", Type.PERSONAL, Status.ACTIVE, 200, Currency.USD, new Timestamp(new Date().getTime())));

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
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Agreement agreement = new Agreement(1, accounts.get(0),
                new Product(5, managers.get(2), "mortgage", Currency.UAH, 20.00,
                        1000000, new Timestamp(new Date().getTime())),
                3.00, Status.ACTIVE, 100000, new Timestamp(new Date().getTime()));
        Account account = accountService.getById(accountId);
        Mockito.when(agreementRepository.findAll()).thenReturn(Arrays.asList(agreement));
        accountService.changeStatus(accountId, Status.WAITING);
        assertEquals(Status.WAITING, account.getStatus());
    }



//    @Test
//    void delete() {
//        UUID accountId = accounts.get(1).getId();
//        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(1)));
//        Account account = accountService.getById(accountId);
//        accountService.delete(accountId);
//        Mockito.verify(accountRepository).delete(account);
//    }

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

//    @Override
//    public List<Transaction> getHistoryOfTransactionsByAccountId(UUID id) {
//        Account account = getById(id);
//        List<Transaction> allTransactions = transactionService.getAll();
//        List<Transaction> creditTransactions = allTransactions.stream()
//                .filter(transaction -> transaction.getCreditAccount()
//                        .getId().equals(id)).toList();
//        List<Transaction> debitTransactions = allTransactions.stream()
//                .filter(transaction -> transaction.getDebitAccount()
//                        .getId().equals(id)).toList();
//        List<Transaction> resultedList = Stream.concat(debitTransactions.stream(), creditTransactions.stream()).collect(Collectors.toList());
//        if (resultedList.isEmpty()) {
//            throw new EmptyRequiredListException("There is no one registered transaction for account with id " + id);
//        }
//        return resultedList;
//    }
//
////UUID debitAccountId, UUID creditAccountId, double sum, Type type, String description
//    @Override
//    @Transactional
//    public void transferMoneyBetweenAccounts(Transaction transaction) {
//        double sum = transaction.getAmount();
//        UUID debitAccountId = transaction.getDebitAccount().getId();
//        UUID creditAccountId = transaction.getCreditAccount().getId();
//        if (sum < 0) {
//            throw new WrongValueException("Unable to transfer negative amount");
//        }
//        if (!accountIsValid(debitAccountId)) {
//            throw new InvalidStatusException("Debit account is not allowed to sent money.");
//        }
//        if (!accountIsValid(creditAccountId)) {
//            throw new InvalidStatusException("Credit account is not allowed to accept money.");
//        }
//        Account debitAccount = getById(debitAccountId);
//        Account creditAccount = getById(creditAccountId);
//        double convertedSum = sum;
//        if (needsToBeConverted(debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode())) {
//            convertedSum = converter.convertCurrency(sum, debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode());
//        }
//        ;
//        if ((debitAccount.getBalance() - sum) < 0) {
//            throw new NotEnoughMoneyException(String.format(
//                    "There is not enough money to transfer on the chosen account: %s", debitAccountId));
//        }
//        debitAccount.setBalance(debitAccount.getBalance() - sum);
//        debitAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//        creditAccount.setBalance(creditAccount.getBalance() + convertedSum);
//        creditAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//        accountRepository.save(debitAccount);
//        accountRepository.save(creditAccount);
//        Transaction newTransaction = new Transaction(debitAccount, creditAccount, transaction.getType(), sum, transaction.getDescription());
//        transactionService.save(newTransaction);
//    }
//

//
//
//    @Override
//    @Transactional
//    public Account withdrawMoney(UUID clientUniqueId, UUID accountId, double sum) {
//        double initSum = sum;
//        Account foundAccount = getById(accountId);
//        if (accountBelongsToClient(clientUniqueId, accountId) && accountIsValid(accountId)) {
//            if ((foundAccount.getBalance() - sum) < 0) {
//                throw new NotEnoughMoneyException("There is not enough money to withdraw.");
//            }
//            sum = foundAccount.getBalance() - sum;
//            foundAccount.setBalance(sum);
//            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//            accountRepository.save(foundAccount);
//            Transaction newTransaction = new Transaction(getById(accountId), accountRepository.findAll().get(0), Type.INNERBANK, initSum, "bank account withdrawal");
//            transactionService.save(newTransaction);
//        }
//        return foundAccount;
//    }
//
//    @Override
//    @Transactional
//    public Account putMoney(UUID accountId, double sum) {
//        Account foundAccount = getById(accountId);
//        if (accountIsValid(accountId)) {
//            foundAccount.setBalance(sum + foundAccount.getBalance());
//            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//            accountRepository.save(foundAccount);
//            Transaction newTransaction = new Transaction(accountRepository.findAll().get(0), getById(accountId), Type.INNERBANK, sum, "bank account replenishment");
//            transactionService.save(newTransaction);
//        }
//        return foundAccount;
//    }

    @Test
    void getHistoryOfTransactionsByAccountId() {
    }

    @Test
    void transferMoneyBetweenAccounts() {
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
    }

    @Test
    void putMoney() {
    }

    @Test
    void inactivateAccount() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Agreement agreement = new Agreement(1, accounts.get(0),
                new Product(5, managers.get(2), "mortgage", Currency.UAH, 20.00,
                        1000000, new Timestamp(new Date().getTime())),
                3.00, Status.ACTIVE, 100000, new Timestamp(new Date().getTime()));
        Mockito.when(agreementRepository.findAll()).thenReturn(Arrays.asList(agreement));
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
