package telran.functionality.com.service;
/**
 * Class implementing AccountService to manage information for accounts including transactions
 * @see telran.functionality.com.service.AccountService
 *
 * @author Olena Averchenko
 */
import lombok.RequiredArgsConstructor;
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


import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AgreementRepository agreementRepository;

    private final TransactionService transactionService;

    /**
     * Method to get all the accounts from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested accounts
     */
    @Override
    public List<Account> getAll() {
        List<Account> allAccounts = accountRepository.findAll();
        if (allAccounts.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered account");
        }
        return allAccounts;
    }

    /**
     * Method to get the account by its id
     * @param id unique id for the account
     * @throws NotExistingEntityException if it doesn't exist
     * @return found account
     */
    @Override
    public Account getById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotExistingEntityException(
                        String.format("Account with id %s doesn't exist", id)));
    }

    /**
     * Method to save a new account
     * @param account new client
     * @return Account saved account
     */
    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Method to change status for the chosen account
     * @param id unique account id
     * @param newStatus new Status
     * @return account with changed status
     */
    @Override
    public Account changeStatus(UUID id, Status newStatus) {
        Account currentAccount = getById(id);
        Agreement agreement = currentAccount.getAgreement();
        if(agreement == null) {
            throw new AccountIsNotValidException("Account has not activated yet. There is no active agreement.");
        }
        currentAccount.setStatus(newStatus);
        currentAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        accountRepository.save(currentAccount);
        return currentAccount;
    }

    /**
     * Method to delete account by its id
     * @param id unique id for the account
     */
    @Override
    @Transactional
    public void delete(UUID id) {
        Account account = getById(id);

        if (account.getId().equals(getAll().get(0).getId())) {
            throw new ForbiddenDeleteAttemptException("Unable to delete bank account. It belongs to the bank.");
        }
        if (account.getBalance() != 0) {
            throw new NotEmptyBalanceOfAccountException("Please, withdraw money from your account before deleting.");
        }
        accountRepository.delete(account);
    }

    /**
     * Method to get a balance of the found by id account
     * @param id unique id for the account
     * @return BalanceDto the object containing the necessary information
     */
    @Override
    public BalanceDto getBalanceOf(UUID id) {
        Account currentAccount = getById(id);
        return new BalanceDto(id, currentAccount.getBalance(), currentAccount.getCurrencyCode());
    }

    /**
     * Method to get the history of Transactions for the account, found by the id
     * @param id unique id for the account
     * @throws EmptyRequiredListException if there is no one transaction for this account
     * @return list of income and outcome transfers for the certain account
     */
    @Override
    public List<Transaction> getHistoryOfTransactionsByAccountId(UUID id) {
        Account account = getById(id);
        List<Transaction> allTransactions = transactionService.getAll();
        List<Transaction> creditTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getCreditAccount()
                        .getId().equals(id)).toList();
        List<Transaction> debitTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getDebitAccount()
                        .getId().equals(id)).toList();
        List<Transaction> resultedList = Stream.concat(debitTransactions.stream(), creditTransactions.stream()).collect(Collectors.toList());
        if (resultedList.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered transaction for account with id " + id);
        }
        return resultedList;
    }

    /**
     * Method to transfer money between accounts
     * @param transaction provided by user information about debit account, credit account, sum to transfer, type of transaction and the description
     * @throws WrongValueException if user is trying to transfer a negative amount of money
     * @throws InvalidStatusException if debit account or credit account has an invalid status
     * @throws NotEnoughMoneyException if there is not enough money to transfer
     * @return new transaction, describing current transfer
     */
    @Override
    @Transactional
    public Transaction transferMoneyBetweenAccounts(Transaction transaction) {
        double sum = transaction.getAmount();
        UUID debitAccountId = transaction.getDebitAccount().getId();
        UUID creditAccountId = transaction.getCreditAccount().getId();
        if (sum < 0) {
            throw new WrongValueException("Unable to transfer negative amount");
        }
        if (!accountIsValid(debitAccountId)) {
            throw new InvalidStatusException("Debit account is not allowed to sent money.");
        }
        if (!accountIsValid(creditAccountId)) {
            throw new InvalidStatusException("Credit account is not allowed to accept money.");
        }
        Account debitAccount = getById(debitAccountId);
        Account creditAccount = getById(creditAccountId);
        double convertedSum = sum;
        if (needsToBeConverted(debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode())) {
            convertedSum = CurrencyConverter.convertCurrency(sum, debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode());
        }
        ;
        if ((debitAccount.getBalance() - sum) < 0) {
            throw new NotEnoughMoneyException(String.format(
                    "There is not enough money to transfer on the chosen account: %s", debitAccountId));
        }
        debitAccount.setBalance(debitAccount.getBalance() - sum);
        debitAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        creditAccount.setBalance(creditAccount.getBalance() + convertedSum);
        creditAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        Transaction newTransaction = new Transaction(debitAccount, creditAccount, transaction.getType(), sum, transaction.getDescription());
        transactionService.save(newTransaction);
        return newTransaction;
    }

    /**
     * Method to check if the account belongs to the client
     * @param clientId id of the client, expected to be the owner of the following account
     * @param accountId id of the account
     * @throws AccountDoesntBelongToClientException if this client is not the owner of the account
     * @return true if the account belongs to the client
     */
    @Override
    public boolean accountBelongsToClient(UUID clientId, UUID accountId) {
        Account foundAccount = getById(accountId);
        if (!foundAccount.getClient().getId().equals(clientId)) {
            throw new AccountDoesntBelongToClientException(String.format("Account with id %s doesn't belong to client with id %s", accountId, clientId));
        }
        return true;
    }

    /**
     * Method to withdraw money from the account
     * @param clientUniqueId id of the client, expected to be the owner of the following account
     * @param accountId id of the account to withdraw money from
     * @param sum amount of money to withdraw
     * @throws WrongValueException if user is trying to withdraw a negative amount of money
     * @throws InvalidStatusException if the account has an invalid status
     * @throws NotEnoughMoneyException if there is not enough money to transfer
     * @return the updated account with updated sum
     */
    @Override
    @Transactional
    public Account withdrawMoney(UUID clientUniqueId, UUID accountId, double sum) {
        double initSum = sum;
        if (sum < 0) {
            throw new WrongValueException("Unable to withdraw a negative amount");
        }
        Account foundAccount = getById(accountId);
        if (accountBelongsToClient(clientUniqueId, accountId) && accountIsValid(accountId)) {
            if ((foundAccount.getBalance() - sum) < 0) {
                throw new NotEnoughMoneyException("There is not enough money to withdraw.");
            }
            sum = foundAccount.getBalance() - sum;
            foundAccount.setBalance(sum);
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//            accountRepository.save(foundAccount);
            Transaction newTransaction = new Transaction(foundAccount, accountRepository.findAll().get(0), Type.INNERBANK, initSum, "bank account withdrawal");
            transactionService.save(newTransaction);
        }
        return foundAccount;
    }

    /**
     * Method to put money to the account
     * @param accountId id of the account to put money to
     * @param sum amount of money to put
     * @return the updated account with the updated sum
     */
    @Override
    @Transactional
    public Account putMoney(UUID accountId, double sum) {
        Account foundAccount = getById(accountId);
        if (accountIsValid(accountId)) {
            foundAccount.setBalance(sum + foundAccount.getBalance());
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
//            accountRepository.save(foundAccount);
            Transaction newTransaction = new Transaction(accountRepository.findAll().get(0), foundAccount, Type.INNERBANK, sum, "bank account replenishment");
            transactionService.save(newTransaction);
        }
        return foundAccount;
    }

    /**
     * Method to make the account inactive
     * @param id unique account id
     */
    @Override
    public void inactivateAccount(UUID id) {
        changeStatus(id, Status.INACTIVE);
    }

    /**
     * Method to check if the required account exists
     * @param account account object to be checked
     * @throws NotExistingEntityException if account doesn't exist
     * @return true, if the account exists
     */
    public boolean accountExists(Account account) {
        if (account == null) {
            throw new NotExistingEntityException("Account doesn't exist");
        }
        return true;
    }

    /**
     * Method to check if the status of the chosen account is active
     * @param id account id
     * @throws InvalidStatusException if the account has an inactive status
     * @return true if the account has an active status
     */
    public boolean accountIsValid(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        ;
        if (accountExists(account)) {
            if (account.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the account with id %s. It's not active.", id));
            }
        }
        return true;
    }

    /**
     * Method to check if the currency needs to be converted during the transaction attempt
     * @param from currency of debit account
     * @param to currency of credit account
     * @return true if the currencies are not the same, or false, if they are different
     */
    public boolean needsToBeConverted(Currency from, Currency to) {
        if (from.name().equals(to.name())) {
            return false;
        }
        return true;
    }
}
