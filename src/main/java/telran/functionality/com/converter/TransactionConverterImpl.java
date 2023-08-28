package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountCreateDto;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionCreateDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.repository.AccountRepository;
import telran.functionality.com.service.AccountService;


@Component
public class TransactionConverterImpl implements Converter<Transaction, TransactionDto, TransactionCreateDto> {


    @Autowired
    private Converter<Account, AccountDto, AccountCreateDto> accountConverter;

    @Autowired
    private AccountService accountService;

    @Override
    public TransactionDto toDto(Transaction transaction) {
        TransactionDto newTransactionDto = new TransactionDto(transaction.getUniqueTransactionId(),
                new AccountDto(transaction.getDebitAccount().getUniqueAccountId(),
                        null,
                        transaction.getDebitAccount().getStatus(), null),
                new AccountDto(transaction.getCreditAccount().getUniqueAccountId(),
                        null,
                        transaction.getCreditAccount().getStatus(),
                        null),
                transaction.getAmount(),
                transaction.getCreatedAt());
        return newTransactionDto;
    }


    @Override
    public Transaction toEntity(TransactionCreateDto createdDto) {
        Transaction transaction = new Transaction(accountService.getByIban(createdDto.getDebitAccountId()),
                accountService.getByIban(createdDto.getCreditAccountId()),
                createdDto.getType(), createdDto.getAmount(), createdDto.getDescription());
        return transaction;
    }
}
