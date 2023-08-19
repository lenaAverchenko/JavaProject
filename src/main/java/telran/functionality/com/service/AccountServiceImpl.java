package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.AccountRepository;
;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<Account> getAll() {
        logger.info("Call method getAll accounts in service");
        List<Account> allAccounts = accountRepository.findAll();
        if (allAccounts.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered account");
        }
        return allAccounts;
    }

    @Override
    public Account getById(UUID id) {
        logger.info("Call method getById {} account in service", id);
        Account foundAccount = accountRepository.findById(id).orElse(null);
        if (foundAccount == null) {
            throw new NotExistingEntityException(
                    String.format("Account with id %s doesn't exist", id));
        }
        return foundAccount;
    }

    @Override
    public Account create(Account account) {
        logger.info("Call method create account in service");
        return accountRepository.save(account);
    }


    @Override
    public Account changeStatus(UUID id, int newStatus) {
        logger.info("Call method changeStatus of account in service from {} to {}", getById(id).getStatus(), newStatus);
        if (newStatus < 0 || newStatus > 2) {
            throw new WrongValueException("Status can only be: 0, 1, 2");
        }
        Account currentAccount = getById(id);
        currentAccount.setStatus(newStatus);
        currentAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        accountRepository.save(currentAccount);
        return currentAccount;
    }

    @Override
    public void delete(UUID id) {
        logger.info("Call method delete account by id {} in service", id);
        Account account = getById(id);
        accountRepository.delete(account);
    }

    @Override
    public double getBalanceOf(UUID id) {
        logger.info("Call method getBalanceOf account by id {} in service", id);
        return getById(id).getBalance();
    }

    @Override
    public List<Transaction> getHistoryOfTransactionsByAccountId(UUID id) {
        logger.info("Call method getHistoryOfTransactionsByAccountId {} in service", id);
        Account account = getById(id);
        List<Transaction> allTransactions = transactionService.getAll();
        List<Transaction> creditTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getCreditAccount()
                        .getId().equals(id)).toList();
        List<Transaction> debitTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getDebitAccount()
                        .getId().equals(id)).toList();
        return Stream.concat(debitTransactions.stream(), creditTransactions.stream()).collect(Collectors.toList());
    }

    @Override
    public void transferMoneyBetweenAccounts(UUID debitAccountId, UUID creditAccountId, double sum, int type, String description) {
        logger.info("Call method transferMoneyBetweenAccounts in service");
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
        if ((debitAccount.getBalance() - sum) < 0) {
            throw new NotEnoughMoneyException(String.format(
                    "There is not enough money to transfer on the chosen account: %s", debitAccountId));
        }
        debitAccount.setBalance(debitAccount.getBalance() - sum);
        debitAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        creditAccount.setBalance(creditAccount.getBalance() + sum);
        creditAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
        Transaction newTransaction = new Transaction(debitAccount, creditAccount, type, sum, description);
        transactionService.save(newTransaction);
    }

    @Override
    public boolean accountBelongsToClient(UUID clientId, UUID accountId) {
        logger.info("Call method accountBelongsToClient in service for accountId: {} and clientId: {}", accountId, clientId);
        Account foundAccount = getById(accountId);
        if (!foundAccount.getClient().getId().equals(accountId)) {
            throw new AccountDoesntBelongToClientException(String.format("Account with id %s doesn't belong to client with id %s", accountId, clientId));
        }
        logger.info("Method accountBelongsToClient has resulted with: true");
        return true;
    }

    @Override
    public Account withdrawMoney(UUID clientId, UUID accountId, double sum) {
        logger.info("Call method withdrawMoney {} in service from accountId: {}", sum, accountId);
        Account foundAccount = getById(accountId);
        if (accountBelongsToClient(clientId, accountId) && accountIsValid(accountId)) {
            if ((foundAccount.getBalance() - sum) < 0) {
                throw new NotEnoughMoneyException("There is not enough money to withdraw.");
            }
            sum = foundAccount.getBalance() - sum;
            foundAccount.setBalance(sum);
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
            accountRepository.save(foundAccount);
        }
        return foundAccount;
    }

    @Override
    public Account putMoney(UUID accountId, double sum) {
        logger.info("Call method putMoney {} in service to accountId: {}", sum, accountId);
        Account foundAccount = accountRepository.getReferenceById(accountId);
        if (accountIsValid(accountId)) {
            foundAccount.setBalance(sum + foundAccount.getBalance());
            foundAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
            accountRepository.save(foundAccount);
        }
        return foundAccount;
    }

    @Override
    public void inactivateAccount(UUID id) {
        logger.info("Call method inactivateAccount in account service");
        changeStatus(id, 0);
    }


    public boolean accountExists(Account account) {
        logger.info("Call method accountExists in account service");
        if (account == null) {
            throw new NotExistingEntityException("Account doesn't exist");
        }
        return true;
    }

    public boolean accountIsValid(UUID id) {
        logger.info("Call method accountIsValid in account service");
        Account account = accountRepository.findById(id).orElse(null);
        if (accountExists(account)) {
            if (account.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Unable to get access to the account with id %s.", id));
            }
        }
        return true;
    }
}
