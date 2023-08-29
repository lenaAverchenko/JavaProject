package telran.functionality.com.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.AgreementRepository;
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
    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public List<Client> getAll() {
        List<Client> allClients = clientRepository.findAll();
        if (allClients.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered client");
        }
         return allClients;
    }

    @Override
    public Client getById(UUID uniqueId) {
        Client foundClient = getAll().stream().filter(client -> client.getUniqueClientId().equals(uniqueId))
                .findFirst().orElse(null);
        if (foundClient == null) {
            throw new NotExistingEntityException(
                    String.format("Client with id %s doesn't exist", uniqueId));
        }
        return foundClient;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

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

    @Override
    @Transactional
    public void delete(UUID id) {
        Client foundClient = getById(id);
        if (foundClient.getId() == 1){
            throw new ForbiddenDeleteAttemptException("Unable to delete bank client. It belongs to the bank.");
        }

        clientRepository.delete(foundClient);
    }

    @Override
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

    @Override
    public void changeStatus(UUID clientId, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        Client client = getById(clientId);
        client.setStatus(status);
        client.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(client);
    }

    @Override
    public void inactivateStatus(UUID id) {
        changeStatus(id, Status.INACTIVE);
    }

    public boolean clientExists(Client client) {
        if (client == null) {
            throw new NotExistingEntityException("Client doesn't exist");
        }
        return true;
    }

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
