package telran.functionality.com.converter;

/**
 * Class - Converter for the Client entities
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Client;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientConverterImpl implements Converter<Client, ClientDto, ClientCreateDto> {

    private final Converter<Account, AccountDto, AccountCreateDto> accountConverter;

    private final ManagerRepository managerRepository;

    /**
     * Method to convert Client entity to ClientDto, showed to user.
     *
     * @param client entity from database
     * @return ClientDto object
     */
    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getStatus(),
                client.getFirstName(), client.getLastName(),
                new ManagerDto(client.getManager().getId(),
                        client.getManager().getFirstName(),
                        client.getManager().getLastName(),
                        client.getManager().getStatus(),
                        null, null),
                client.getAccounts().stream().map(accountConverter::toDto)
                        .collect(Collectors.toList()));
    }

    /**
     * Method to convert ClientCreateDto (data provided by user) to entity Client, stored in database.
     *
     * @param createdDto provided data
     * @return Client entity
     */
    @Override
    public Client toEntity(ClientCreateDto createdDto) {
        return new Client(managerRepository.getReferenceById(createdDto.getManagerId()),
                new ArrayList<>(), createdDto.getTaxCode(),
                createdDto.getFirstName(), createdDto.getLastName(), createdDto.getEmail(),
                createdDto.getAddress(), createdDto.getPhone(), new Timestamp(new Date().getTime()));
    }
}