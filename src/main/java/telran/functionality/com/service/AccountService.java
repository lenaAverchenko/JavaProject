package telran.functionality.com.service;

import telran.functionality.com.dto.BalanceDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<Account> getAll();

    Account getByIban(UUID iban);

    Account save(Account account);

    Account changeStatus(UUID iban, Status newStatus);

    void delete(UUID iban);

    BalanceDto getBalanceOf(UUID iban);

    List<Transaction> getHistoryOfTransactionsByAccountIban(UUID iban);

    void transferMoneyBetweenAccounts(UUID debitAccountIban, UUID creditAccountIban, double sum, Type type, String description);

    Account withdrawMoney(UUID clientId, UUID accountIban, double sum);

    Account putMoney(UUID accountIban, double sum);

    boolean accountIsValid(UUID iban);

    void inactivateAccount(UUID iban);

    boolean accountBelongsToClient(UUID clientId, UUID accountIban);



}
