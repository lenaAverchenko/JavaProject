package telran.functionality.com.converter;

import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.entity.Client;

public interface ClientConverter {
    ClientDto toDto(Client client);

    Client toEntity(ClientDto clientDto);
}
