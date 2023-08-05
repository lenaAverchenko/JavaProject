package telran.functionality.com.service;

import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<Transaction> getAll();

    Transaction getById(UUID id);

}
