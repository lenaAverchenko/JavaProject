package telran.functionality.com.converter;
/**
 * Class - Converter for the Transaction entities
 *
 * @author Olena Averchenko
 */
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionCreateDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.service.AccountService;

@Component
@RequiredArgsConstructor
public class TransactionConverterImpl implements Converter<Transaction, TransactionDto, TransactionCreateDto> {

    private final AccountService accountService;

    /**
     * Method to convert Transaction entity to TransactionDto, showed to user.
     * @param transaction entity from database
     * @return TransactionDto object
     */
    @Override
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(),
                new AccountDto(transaction.getDebitAccount().getId(),
                        null,
                        transaction.getDebitAccount().getStatus(), null),
                new AccountDto(transaction.getCreditAccount().getId(),
                        null,
                        transaction.getCreditAccount().getStatus(),
                        null),
                transaction.getAmount(),
                transaction.getCreatedAt());
    }

    /**
     * Method to convert TransactionCreateDto (data provided by user) to entity Transaction, stored in database.
     * @param createdDto provided data
     * @return Transaction entity
     */
    @Override
    public Transaction toEntity(TransactionCreateDto createdDto) {
        return new Transaction(accountService.getById(createdDto.getDebitAccountId()),
                accountService.getById(createdDto.getCreditAccountId()),
                createdDto.getType(), createdDto.getAmount(), createdDto.getDescription());
    }
}