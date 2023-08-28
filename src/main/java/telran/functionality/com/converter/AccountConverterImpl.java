package telran.functionality.com.converter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountCreateDto;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.service.ClientService;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class AccountConverterImpl implements Converter<Account, AccountDto, AccountCreateDto> {


    @Autowired
    private ClientService clientService;

    @Override
    public AccountDto toDto(Account account) {
        AccountDto newAccountDto = new AccountDto(account.getUniqueAccountId(), account.getName(), account.getStatus(),
                new ClientDto(account.getClient().getUniqueClientId(), account.getClient().getStatus(), account.getClient().getFirstName(),
                        account.getClient().getLastName(), null, null));
        return newAccountDto;
    }

    @Override
    public Account toEntity(AccountCreateDto createdDto) {
        Account newAccount = new Account(clientService.getById(createdDto.getClientId()),
                createdDto.getName(), createdDto.getType(), createdDto.getBalance(),
                createdDto.getCurrencyCode(), new Timestamp(new Date().getTime()));
        return newAccount;
    }

}
