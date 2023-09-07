package telran.functionality.com.service;
/**
 * Interface ClientService
 *
 * @author Olena Averchenko
 */

import telran.functionality.com.entity.Client;
import telran.functionality.com.enums.Status;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<Client> getAll();

    Client getById(UUID id);

    Client save(Client client);

    Client updatePersonalInfo(UUID id, Client client);

    void delete(UUID id);

    void changeManager(UUID clientId, long managerId);

    void changeStatus(UUID clientId, Status status);

    void inactivateStatus(UUID id);

}
