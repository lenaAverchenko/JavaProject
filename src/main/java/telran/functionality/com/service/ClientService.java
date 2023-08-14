package telran.functionality.com.service;


import telran.functionality.com.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<Client> getAll();

    Client getById(UUID id);

    Client create(Client account);

    Client update(UUID id, Client client);

    void delete(UUID id);
}
