package telran.functionality.com.converter;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.repository.AccountRepository;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientConverterImpl implements Converter<Client, ClientDto, ClientCreateDto> {

    private static final Logger logger = LoggerFactory.getLogger(ClientConverterImpl.class);

    @Autowired
    private final Converter<Account, AccountDto, AccountCreateDto> accountConverter;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public ClientDto toDto(Client client) {
        logger.debug("Call method toDto for Client: {}", client);
        ClientDto clientDto = new ClientDto(client.getId(), client.getStatus(),
                client.getFirstName(), client.getLastName(),
                new ManagerDto(client.getManager().getId(),
                        client.getManager().getFirstName(),
                        client.getManager().getLastName(),
                        client.getManager().getStatus(),
                        null, null),
                client.getAccounts().stream().map(accountConverter::toDto)
                        .collect(Collectors.toList()));
        logger.debug("Method toDto has ended with the result: {}", clientDto);
        return clientDto;
    }

    @Override
    public Client toEntity(ClientCreateDto createdDto) {
        logger.debug("Call method toEntity for Client: {}", createdDto);
        Client client = new Client(managerRepository.getReferenceById(createdDto.getManagerId()),
                new ArrayList<>(), createdDto.getTaxCode(),
                createdDto.getFirstName(), createdDto.getLastName(), createdDto.getEmail(),
                createdDto.getAddress(), createdDto.getPhone(), new Timestamp(new Date().getTime()));
        logger.debug("Method toEntity has ended with the result: {}", client);
        return client;
    }
}
