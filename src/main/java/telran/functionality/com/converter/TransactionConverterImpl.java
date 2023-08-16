package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;

@Component
public class TransactionConverterImpl implements Converter<Transaction, TransactionDto> {
    @Autowired
    private Converter<Account, AccountDto> accountConverter;

    @Override
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(),
                new AccountDto(transaction.getDebitAccount().getId(),
                        null, null, transaction.getDebitAccount().getBalance()),
                new AccountDto(transaction.getCreditAccount().getId(),
                        null, null, transaction.getCreditAccount().getBalance()),
                transaction.getAmount(),
                transaction.getCreatedAt());
    }

    /* Todo */

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        return new Transaction(transactionDto.getId(), accountConverter.toEntity(transactionDto.getDebitAccount()),
                accountConverter.toEntity(transactionDto.getCreditAccount()), transactionDto.getAmount());
    }
}
