package telran.functionality.com.service;


import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<Client> getAll();

    Client getById(UUID id);

    Client create(Client account);

    Client updatePersonalInfo(UUID id, Client client);

    void delete(UUID id);

    void changeManager(UUID clientId, long managerId);

    void changeStatus(UUID clientId, int status);

    void inactivateStatus(UUID id);

}
