package telran.functionality.com.converter;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountCreateDto;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Client;
import telran.functionality.com.repository.ClientRepository;
import telran.functionality.com.service.ClientService;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccountConverterImpl implements Converter<Account, AccountDto, AccountCreateDto> {

    private static final Logger logger = LoggerFactory.getLogger(AccountConverterImpl.class);

    @Autowired
    private ClientService clientService;

    @Override
    public AccountDto toDto(Account account) {
        logger.debug("Call method toDto for Account: {}", account);
        AccountDto newAccountDto = new AccountDto(account.getId(), account.getName(), account.getStatus(),
                new ClientDto(account.getClient().getId(), account.getClient().getStatus(), account.getClient().getFirstName(),
                        account.getClient().getLastName(), null, null), account.getBalance());
        logger.debug("Method toDto has ended with the result: {}", newAccountDto);
        return newAccountDto;
    }

    @Override
    public Account toEntity(AccountCreateDto createdDto) {
        logger.debug("Call method toEntity for Account: {}", createdDto);
        Account newAccount = new Account(clientService.getById(createdDto.getClientId()),
                createdDto.getName(), createdDto.getType(), createdDto.getBalance(),
                createdDto.getCurrencyCode(), new Timestamp(new Date().getTime()));
        logger.debug("Method toEntity has ended with the result: {}", newAccount);
        return newAccount;
    }

}
