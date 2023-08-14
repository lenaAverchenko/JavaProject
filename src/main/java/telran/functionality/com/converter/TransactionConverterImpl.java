package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Transaction;

@Component
public class TransactionConverterImpl implements TransactionConverter {
    @Autowired
    private AccountConverter accountConverter;
    @Override
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(), accountConverter.toDto(transaction.getDebitAccount()),
                accountConverter.toDto(transaction.getCreditAccount()), transaction.getAmount(),
                transaction.getCreatedAt());
    }

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        return new Transaction(transactionDto.getId(), accountConverter.toEntity(transactionDto.getDebitAccount()),
                accountConverter.toEntity(transactionDto.getCreditAccount()), transactionDto.getAmount());
    }
}
