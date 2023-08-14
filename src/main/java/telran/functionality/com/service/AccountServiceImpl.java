package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.repository.AccountRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getById(UUID id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }


    @Override
    public Account update(UUID id, Account account) {
        List <Account> accounts = getAll();
        for (Account currentAccount: accounts) {
            if (currentAccount.getId().equals(id)) {
                currentAccount.setName(account.getName());
                currentAccount.setType(account.getType());
                currentAccount.setStatus(account.getStatus());
                currentAccount.setBalance(account.getBalance());
                currentAccount.setCurrencyCode(account.getCurrencyCode());
                currentAccount.setUpdatedAt(new Timestamp(new Date().getTime()));
                return currentAccount;
            }
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        accountRepository.deleteById(id);
    }

    @Override
    public double getBalanceOf(UUID id) {
        return getById(id).getBalance();
    }

    @Override
    public List<Transaction> getHistoryOfTransactionsByAccountId(UUID id) {
        List<Transaction> allTransactions = transactionService.getAll();
        List<Transaction> creditTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getCreditAccount()
                .getId().equals(id)).toList();
        List<Transaction> debitTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getDebitAccount()
                        .getId().equals(id)).toList();
        return Stream.concat(debitTransactions.stream(), creditTransactions.stream()).collect(Collectors.toList());
    }

}
