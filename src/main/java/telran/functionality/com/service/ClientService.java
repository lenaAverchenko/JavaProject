package telran.functionality.com.service;


import telran.functionality.com.entity.Client;
import telran.functionality.com.enums.Status;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<Client> getAll();

    Client getById(UUID uniqueId);

    Client save(Client client);

    Client updatePersonalInfo(UUID uniqueId, Client client);

    void delete(UUID uniqueId);

    void changeManager(UUID clientId, long managerId);

    void changeStatus(UUID clientId, Status status);

    void inactivateStatus(UUID uniqueId);

}
