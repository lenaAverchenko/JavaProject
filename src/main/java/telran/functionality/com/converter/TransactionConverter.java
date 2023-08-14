package telran.functionality.com.converter;

import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Transaction;

public interface TransactionConverter {

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);
}
