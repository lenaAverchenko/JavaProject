package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Client;
import telran.functionality.com.repository.ClientRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(UUID id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }


    @Override
    public Client update(UUID id, Client client) {
        List <Client> clients = getAll();
        for (Client currentClient: clients) {
            if (currentClient.getId().equals(id)) {
                currentClient.setManager(client.getManager());
                currentClient.setAccounts(client.getAccounts());
                currentClient.setStatus(client.getStatus());
                currentClient.setTaxCode(client.getTaxCode());
                currentClient.setFirstName(client.getFirstName());
                currentClient.setLastName(client.getLastName());
                currentClient.setEmail(client.getEmail());
                currentClient.setPhone(client.getPhone());
                currentClient.setAddress(client.getAddress());
                currentClient.setUpdatedAt(new Timestamp(new Date().getTime()));
                return currentClient;
            }
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        clientRepository.deleteById(id);
    }
}
