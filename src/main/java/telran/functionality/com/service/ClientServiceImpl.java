package telran.functionality.com.service;

/**
 * Class implementing ClientService to manage information about Clients
 * @see telran.functionality.com.service.ClientService
 *
 * @author Olena Averchenko
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.ClientRepository;
import telran.functionality.com.repository.ManagerRepository;


import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {


    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Method to get all the clients from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested clients
     */
    @Override
    public List<Client> getAll() {
        List<Client> allClients = clientRepository.findAll();
        if (allClients.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered client");
        }
        return allClients;
    }

    /**
     * Method to get the client by its id
     * @param id unique id for the client
     * @throws NotExistingEntityException if it doesn't exist
     * @return found client
     */
    @Override
    public Client getById(UUID id) {
        Client foundClient = clientRepository.findById(id).orElse(null);
        if (foundClient == null) {
            throw new NotExistingEntityException(
                    String.format("Client with id %s doesn't exist", id));
        }
        return foundClient;
    }

    /**
     * Method to save a new client
     * @param client new client
     * @return saved client
     */
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Method to update existing client
     * @param id unique id for the client
     * @param client updated client information
     * @return updated client saved in the database
     */
    @Override
    public Client updatePersonalInfo(UUID id, Client client) {
        Client currentClient = getById(id);
        currentClient.setFirstName(client.getFirstName());
        currentClient.setLastName(client.getLastName());
        currentClient.setEmail(client.getEmail());
        currentClient.setPhone(client.getPhone());
        currentClient.setAddress(client.getAddress());
        currentClient.setUpdatedAt(new Timestamp(new Date().getTime()));
        clientRepository.save(currentClient);
        return currentClient;
    }

    /**
     * Method to delete client by its id
     * @param id unique id for the client
     */
    @Override
    @Transactional
    public void delete(UUID id) {
        Client foundClient = getById(id);
        if (foundClient.getId().equals(clientRepository.findAll().get(0).getId())) {
            throw new ForbiddenDeleteAttemptException("Unable to delete bank client. It belongs to the bank.");
        }
        clientRepository.delete(foundClient);
    }

    /**
     * Method to change manager for the chosen client
     * @param clientId unique if of the client
     * @param managerId id of the new manager to the client
     * @throws NotExistingEntityException if this new manager doesn't exist
     * @throws InvalidStatusException if new Manager is not active
     */
    @Override
    @Transactional
    public void changeManager(UUID clientId, long managerId) {
        if (statusIsValid(clientId)) {
            Client foundClient = getById(clientId);
            Manager foundManager = managerRepository.findById(managerId).orElse(null);
            if (foundManager == null) {
                throw new NotExistingEntityException(
                        String.format("Manager with id %d doesn't exist", managerId));
            }
            if (foundManager.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Impossible to set manager with id %d to the client, " +
                        "because this manager is no longer available", managerId));
            }
            foundClient.setManager(foundManager);
            Timestamp time = new Timestamp(new Date().getTime());
            foundClient.setUpdatedAt(time);
            foundManager.setUpdatedAt(time);
            managerRepository.save(foundManager);
            clientRepository.save(foundClient);
        }
    }

    /**
     * Method to change status for the chosen client
     * @param clientId unique client id
     * @param status new Status
     */
    @Override
    public void changeStatus(UUID clientId, Status status) {
        Client client = getById(clientId);
        client.setStatus(status);
        client.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(client);
    }

    /**
     * Method to make the client inactive
     * @param id unique client id
     */
    @Override
    public void inactivateStatus(UUID id) {
        changeStatus(id, Status.INACTIVE);
    }

    /**
     * Method to check if the required client exists
     * @param client client object to be checked
     * @throws NotExistingEntityException if client doesn't exist
     * @return true, if the client exists
     */
    public boolean clientExists(Client client) {
        if (client == null) {
            throw new NotExistingEntityException("Client doesn't exist");
        }
        return true;
    }

    /**
     * Method to check if the status of the chosen client is active
     * @param id client id
     * @throws InvalidStatusException if the client has inactive status
     * @return true if the client has an active status
     */
    public boolean statusIsValid(UUID id) {
        Client client = getById(id);
        if (clientExists(client)) {
            if (client.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the client with id %s.", id));
            }
        }
        return true;
    }
}
