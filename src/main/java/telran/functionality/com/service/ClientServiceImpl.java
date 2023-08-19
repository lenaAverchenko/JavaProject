package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.ClientRepository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerService managerService;

    @Override
    public List<Client> getAll() {
        logger.info("Call method getAll clients in service");
        List<Client> allClients = clientRepository.findAll();
        if (allClients.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered client");
        }
        return allClients;
    }

    @Override
    public Client getById(UUID id) {
        logger.info("Call method getById {} client in service", id);
        Client foundClient = clientRepository.findById(id).orElse(null);
        if (foundClient == null) {
            throw new NotExistingEntityException(
                    String.format("Client with id %s doesn't exist", id));
        }
        return foundClient;
    }

    @Override
    public Client create(Client client) {
        logger.info("Call method create client in service");
        return clientRepository.save(client);
    }

    @Override
    public Client updatePersonalInfo(UUID id, Client client) {
        logger.info("Call method updatePersonalInfo of client in service with id: {}", id);
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

    @Override
    public void delete(UUID id) {
        logger.info("Call method delete client in service with id: {}", id);
        Client foundClient = getById(id);
        clientRepository.delete(foundClient);
    }

    @Override
    public void changeManager(UUID clientId, long managerId) {
        logger.info("Call method changeManager for client in service with id: {} to manager with id: {}", clientId, managerId);
        if (statusIsValid(clientId)) {
            Client foundClient = getById(clientId);
            Manager foundManager = managerService.getById(managerId);
            if (foundManager.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Impossible to set manager with id %d to the client, " +
                        "because this manager is no longer available", managerId));
            }
            foundClient.setManager(foundManager);
            Timestamp time = new Timestamp(new Date().getTime());
            foundClient.setUpdatedAt(time);
            foundManager.setUpdatedAt(time);
            managerService.create(foundManager);
            clientRepository.save(foundClient);
        }
    }

    @Override
    public void changeStatus(UUID clientId, int status) {
        logger.info("Call method changeStatus for client in service with id: {} from {} to {}", clientId, getById(clientId).getStatus(), status);
        if (status < 0 || status > 2) {
            throw new WrongValueException("Status can only be: 0, 1, 2");
        }
        Client client = getById(clientId);
        client.setStatus(status);
        client.setUpdatedAt(new Timestamp(new Date().getTime()));
        create(client);
    }

    @Override
    public void inactivateStatus(UUID id) {
        logger.info("Call method inactivateStatus in client service");
        changeStatus(id, 0);
    }

    public boolean clientExists(Client client) {
        logger.info("Call method clientExists in client service");
        if (client == null) {
            throw new NotExistingEntityException("Client doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(UUID id) {
        logger.info("Call method statusIsValid in client service");
        Client client = clientRepository.findById(id).orElse(null);
        if (clientExists(client)) {
            if (client.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Unable to get access to the client with id %s.", id));
            }
        }
        return true;
    }
}
