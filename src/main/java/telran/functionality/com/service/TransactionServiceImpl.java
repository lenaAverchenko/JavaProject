package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);


    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAll() {
        logger.info("Call method getAll transactions in service");
        List<Transaction> allTransactions = transactionRepository.findAll();
        if (allTransactions.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered transaction");
        }
        return allTransactions;
    }

    @Override
    public Transaction getById(UUID id) {
        logger.info("Call method getById {} transaction in service", id);
        Transaction foundTransaction = transactionRepository.findById(id).orElse(null);
        if (foundTransaction == null) {
            throw new NotExistingEntityException(
                    String.format("Transaction with id %s doesn't exist", id.toString()));
        }
        return foundTransaction;
    }

    @Override
    public Transaction save(Transaction transaction) {
        logger.info("Call method save transaction in service");
        return transactionRepository.save(transaction);
    }
}
