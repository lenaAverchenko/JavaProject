package telran.functionality.com.converter;

import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.entity.Account;

public interface AccountConverter {
    AccountDto toDto(Account account);

    Account toEntity(AccountDto accountDto);
}
