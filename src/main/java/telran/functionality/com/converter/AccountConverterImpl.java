package telran.functionality.com.converter;
/**
 * Class - Converter for the Account entities
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountCreateDto;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.service.ClientService;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccountConverterImpl implements Converter<Account, AccountDto, AccountCreateDto> {

    private final ClientService clientService;

    /**
     * Method to convert Account entity to AccountDto, showed to user.
     *
     * @param account entity from database
     * @return AccountDto object
     */
    @Override
    public AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getName(), account.getStatus(),
                new ClientDto(account.getClient().getId(), account.getClient().getStatus(), account.getClient().getFirstName(),
                        account.getClient().getLastName(), null, null));
    }

    /**
     * Method to convert AccountCreateDto (data provided by user) to entity Account, stored in database.
     *
     * @param createdDto provided data
     * @return Account entity
     */
    @Override
    public Account toEntity(AccountCreateDto createdDto) {
        return new Account(clientService.getById(createdDto.getClientId()),
                createdDto.getName(), createdDto.getType(), createdDto.getBalance(),
                createdDto.getCurrencyCode(), new Timestamp(new Date().getTime()));
    }

}
