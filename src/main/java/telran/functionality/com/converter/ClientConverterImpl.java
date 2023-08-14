package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Client;

import java.util.stream.Collectors;

@Component
public class ClientConverterImpl implements ClientConverter {

    @Autowired
    private ManagerConverter managerConverter;
    @Autowired
    private AccountConverter accountConverter;
    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(),
                managerConverter.toDto(client.getManager()),
                client.getAccounts().stream().map(account -> accountConverter.toDto(account)).collect(Collectors.toList()),
                client.getFirstName(), client.getLastName());
    }

    @Override
    public Client toEntity(ClientDto clientDto) {

        return new Client(clientDto.getId(),
                managerConverter.toEntity(clientDto.getManager()),
                clientDto.getAccounts().stream().map(account -> accountConverter.toEntity(account)).collect(Collectors.toList()),
                clientDto.getFirstName(), clientDto.getLastName());
    }
}
