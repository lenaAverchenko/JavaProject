package telran.functionality.com.service;

import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<Account> getAll();

    Account getById(UUID id);

    Account create(Account account);

    Account changeStatus(UUID id, int newStatus);

    void delete(UUID id);

    double getBalanceOf(UUID id);

    List<Transaction> getHistoryOfTransactionsByAccountId(UUID id);

    void transferMoneyBetweenAccounts(UUID debitAccountId, UUID creditAccountId, double sum, int type, String description);

    Account withdrawMoney(UUID clientId, UUID accountId, double sum);

    Account putMoney(UUID accountId, double sum);

    boolean accountIsValid(UUID id);

    void inactivateAccount(UUID id);

    boolean accountBelongsToClient(UUID clientId, UUID accountId);


}
