package telran.functionality.com.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountCreateDto;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionCreateDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.repository.AccountRepository;


@Component
public class TransactionConverterImpl implements Converter<Transaction, TransactionDto, TransactionCreateDto> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConverterImpl.class);

    @Autowired
    private Converter<Account, AccountDto, AccountCreateDto> accountConverter;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TransactionDto toDto(Transaction transaction) {
        logger.debug("Call method toDto for Transaction: {}", transaction);
        TransactionDto newTransactionDto = new TransactionDto(transaction.getId(),
                new AccountDto(transaction.getDebitAccount().getId(),
                        null,
                        transaction.getDebitAccount().getStatus(), null,
                        transaction.getDebitAccount().getBalance()),
                new AccountDto(transaction.getCreditAccount().getId(),
                        null,
                        transaction.getCreditAccount().getStatus(),
                        null, transaction.getCreditAccount().getBalance()),
                transaction.getAmount(),
                transaction.getCreatedAt());
        logger.debug("Method toDto has ended with the result: {}", newTransactionDto);
        return newTransactionDto;
    }


    @Override
    public Transaction toEntity(TransactionCreateDto createdDto) {
        logger.debug("Call method toEntity for Transaction: {}", createdDto);
        Transaction transaction = new Transaction(accountRepository.getReferenceById(createdDto.getDebitAccountId()),
                accountRepository.getReferenceById(createdDto.getCreditAccountId()),
                createdDto.getType(), createdDto.getAmount(), createdDto.getDescription());
        logger.debug("Method toEntity has ended with the result: {}", transaction);
        return transaction;
    }
}
