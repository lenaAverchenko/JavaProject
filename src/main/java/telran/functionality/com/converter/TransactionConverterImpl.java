package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionCreateDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.service.AccountService;


@Component
public class TransactionConverterImpl implements Converter<Transaction, TransactionDto, TransactionCreateDto> {


    @Autowired
    private AccountService accountService;

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


    @Override
    public Transaction toEntity(TransactionCreateDto createdDto) {
        return new Transaction(accountService.getById(createdDto.getDebitAccountId()),
                accountService.getById(createdDto.getCreditAccountId()),
                createdDto.getType(), createdDto.getAmount(), createdDto.getDescription());
    }
}
