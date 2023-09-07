package telran.functionality.com.service;
/**
 * Class implementing TransactionService to manage information about transactions
 * @see telran.functionality.com.service.TransactionService
 *
 * @author Olena Averchenko
 */
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Method to get all the transactions from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested transactions
     */
    @Override
    public List<Transaction> getAll() {
        List<Transaction> allTransactions = transactionRepository.findAll();
        if (allTransactions.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered transaction");
        }
        return allTransactions;
    }

    /**
     * Method to get the transaction by its id
     * @param id unique id for the transaction
     * @throws NotExistingEntityException if it doesn't exist
     * @return found transaction
     */
    @Override
    public Transaction getById(UUID id) {
        return transactionRepository.findById(id).orElseThrow(() -> new NotExistingEntityException(
                String.format("Transaction with id %s doesn't exist", id)));
    }

    /**
     * Method to save some new transaction
     * @param transaction new transaction
     * @return saved transaction
     */
    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Method to delete transaction by its id
     * @param id unique id for the transaction
     */
    @Override
    public void delete(UUID id) {
        transactionRepository.delete(getById(id));
    }
}