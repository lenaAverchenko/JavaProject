package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.converter.CurrencyConverter;
import telran.functionality.com.dto.BalanceDto;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.AccountRepository;
import telran.functionality.com.repository.AgreementRepository;
import telran.functionality.com.repository.ClientRepository;
;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<Account> getAll() {
        List<Account> allAccounts = accountRepository.findAll();
        if (allAccounts.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered account");
        }
        return allAccounts;
    }

    @Override
    public Account getByIban(UUID iban) {
        Account foundAccount = getAll().stream().filter(acc -> acc.getUniqueAccountId().equals(iban)).findFirst().orElse(null);
        if (foundAccount == null) {
            throw new NotExistingEntityException(
                    String.format("Account with iban %s doesn't exist", iban));
        }
        return foundAccount;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }


    @Override
    public Account changeStatus(UUID iban, Status newStatus) {
        Account currentAccount = getByIban(iban);
        if (!agreementRepository.findAll().stream()
                .map(agreement -> agreement.getAccount().getUniqueAccountId())
                .toList().contains(iban)){
            throw new AccountIsNotValidException("Account has not activated yet. There is no active agreement.");
        }
        if (!Arrays.stream(Status.values()).toList().contains(newStatus)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        currentAccount.setStatus(newStatus);
        currentAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        accountRepository.save(currentAccount);
        return currentAccount;
    }


    @Override
    @Transactional
    public void delete(UUID iban) {
        Account account = getByIban(iban);

        if (account.getId() == 1){
            throw new ForbiddenDeleteAttemptException("Unable to delete bank account. It belongs to the bank.");
        }
        if (account.getBalance() != 0){
            throw new NotEmptyBalanceOfAccountException("Please, withdraw money from your account before deleting.");
        }

        accountRepository.delete(account);
    }

    @Override
    public BalanceDto getBalanceOf(UUID iban) {
        Account currentAccount = getByIban(iban);
        return new BalanceDto(iban, currentAccount.getBalance(), currentAccount.getCurrencyCode());
    }

    @Override
    public List<Transaction> getHistoryOfTransactionsByAccountIban(UUID iban) {
        Account account = getByIban(iban);
        List<Transaction> allTransactions = transactionService.getAll();
        List<Transaction> creditTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getCreditAccount()
                        .getUniqueAccountId().equals(iban)).toList();
        List<Transaction> debitTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getDebitAccount()
                        .getUniqueAccountId().equals(iban)).toList();
        List<Transaction> resultedList = Stream.concat(debitTransactions.stream(), creditTransactions.stream()).collect(Collectors.toList());
        if (resultedList.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered transaction for account with iban " + iban);
        }
        return resultedList;
    }


    @Override
    @Transactional
    public void transferMoneyBetweenAccounts(UUID debitAccountIban, UUID creditAccountIban, double sum, Type type, String description) {
        if (sum < 0) {
            throw new WrongValueException("Unable to transfer negative amount");
        }
        if (!accountIsValid(debitAccountIban)) {
            throw new InvalidStatusException("Debit account is not allowed to sent money.");
        }
        if (!accountIsValid(creditAccountIban)) {
            throw new InvalidStatusException("Credit account is not allowed to accept money.");
        }
        Account debitAccount = getByIban(debitAccountIban);
        Account creditAccount = getByIban(creditAccountIban);
        double convertedSum = sum;
        if(needsToBeConverted(debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode())){
            convertedSum = converter.convertCurrency(sum, debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode());
        };
        if ((debitAccount.getBalance() - sum) < 0) {
            throw new NotEnoughMoneyException(String.format(
                    "There is not enough money to transfer on the chosen account: %s", debitAccountIban));
        }
        debitAccount.setBalance(debitAccount.getBalance() - sum);
        debitAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        creditAccount.setBalance(creditAccount.getBalance() + convertedSum);
        creditAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
        Transaction newTransaction = new Transaction(debitAccount, creditAccount, type, sum, description);
        transactionService.save(newTransaction);
    }

    @Override
    public boolean accountBelongsToClient(UUID clientUniqueId, UUID accountIban) {
        Account foundAccount = getByIban(accountIban);
        if (!foundAccount.getClient().getUniqueClientId().equals(clientUniqueId)) {
            throw new AccountDoesntBelongToClientException(String.format("Account with iban %s doesn't belong to client with id %s", accountIban, clientUniqueId));
        }
        return true;
    }


    @Override
    @Transactional
    public Account withdrawMoney(UUID clientUniqueId, UUID accountIban, double sum) {
        double initSum = sum;
        Account foundAccount = getByIban(accountIban);
        if (accountBelongsToClient(clientUniqueId, accountIban) && accountIsValid(accountIban)) {
            if ((foundAccount.getBalance() - sum) < 0) {
                throw new NotEnoughMoneyException("There is not enough money to withdraw.");
            }
            sum = foundAccount.getBalance() - sum;
            foundAccount.setBalance(sum);
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
            accountRepository.save(foundAccount);
            Transaction newTransaction = new Transaction(getByIban(accountIban),accountRepository.findById(1L).orElse(null),Type.INNERBANK,initSum, "bank account withdrawal");
            transactionService.save(newTransaction);
        }
        return foundAccount;
    }

    @Override
    @Transactional
    public Account putMoney(UUID accountIban, double sum) {
        Account foundAccount = getByIban(accountIban);
        if (accountIsValid(accountIban)) {
            foundAccount.setBalance(sum + foundAccount.getBalance());
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
            accountRepository.save(foundAccount);
            Transaction newTransaction = new Transaction(accountRepository.findById(1L).orElse(null),getByIban(accountIban),Type.INNERBANK,sum , "bank account replenishment");
            transactionService.save(newTransaction);
        }
        return foundAccount;
    }

    @Override
    public void inactivateAccount(UUID iban) {
        changeStatus(iban, Status.INACTIVE);
    }


    public boolean accountExists(Account account) {
        if (account == null) {
            throw new NotExistingEntityException("Account doesn't exist");
        }
        return true;
    }

    public boolean accountIsValid(UUID iban) {
        Account account = accountRepository.findAll().stream().filter(acc -> acc.getUniqueAccountId().equals(iban)).findFirst().orElse(null);;
        if (accountExists(account)) {
            if (account.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the account with id %s. It's not active.", iban));
            }
        }
        return true;
    }

    public boolean needsToBeConverted(Currency from, Currency to){
        if (from.name().equals(to.name())){
            return false;
        }
        return true;
    }
}
