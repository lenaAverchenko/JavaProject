package telran.functionality.com.converter;

import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.entity.Account;

@Component
public class AccountConverterImpl implements AccountConverter {
    @Override
    public AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getClientId(), account.getName(),account.getBalance());
    }

    @Override
    public Account toEntity(AccountDto accountDto) {

        return new Account(accountDto.getId(), accountDto.getClientId(), accountDto.getName(), accountDto.getBalance());
    }
}
