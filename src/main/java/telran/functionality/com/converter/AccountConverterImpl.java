package telran.functionality.com.converter;

import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Client;

@Component
public class AccountConverterImpl implements Converter<Account, AccountDto> {
    @Override
    public AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getName(),
                new ClientDto(account.getClient().getId(), account.getClient().getFirstName(),
                        account.getClient().getLastName(), null, null), account.getBalance());
    }

    @Override
    public Account toEntity(AccountDto accountDto) {
        return new Account(accountDto.getId(), accountDto.getName(),
                new Client(accountDto.getClientDto().getId(), accountDto.getClientDto().getFirstName(),
                        accountDto.getClientDto().getLastName(), null, null), accountDto.getBalance());
    }

}
