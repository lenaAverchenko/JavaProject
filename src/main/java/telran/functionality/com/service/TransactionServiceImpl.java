package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAll() {
        List<Transaction> allTransactions = transactionRepository.findAll();
        if (allTransactions.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered transaction");
        }
        return allTransactions;
    }

    @Override
    public Transaction getById(UUID id) {
        Transaction foundTransaction = transactionRepository.findById(id).orElse(null);
        if (foundTransaction == null) {
            throw new NotExistingEntityException(
                    String.format("Transaction with id %s doesn't exist", id.toString()));
        }
        return foundTransaction;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(UUID id) {
        transactionRepository.delete(getById(id));
    }
}
