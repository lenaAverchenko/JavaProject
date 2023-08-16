package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;

import java.util.stream.Collectors;

@Component
public class ClientConverterImpl implements Converter<Client, ClientDto> {

    @Autowired
    private Converter<Account, AccountDto> accountConverter;

    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getFirstName(), client.getLastName(),
                new ManagerDto(client.getManager().getId(),
                        client.getManager().getFirstName(),
                        client.getManager().getLastName(),
                        null, null),
                client.getAccounts().stream().map(account -> accountConverter.toDto(account))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Client toEntity(ClientDto clientDto) {

        return new Client(clientDto.getId(),
                clientDto.getFirstName(), clientDto.getLastName(),
                new Manager(clientDto.getManager().getId(),
                        clientDto.getManager().getFirstName(), clientDto.getManager().getLastName(),
                        null, null),
                clientDto.getAccounts().stream().map(account -> accountConverter.toEntity(account))
                        .collect(Collectors.toList()));
    }
}


//    @Override
//    public Client toEntity(ClientDto clientDto) {
//        throw new UnsupportedOperationException();
//    }
