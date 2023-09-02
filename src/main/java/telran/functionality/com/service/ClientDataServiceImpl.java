package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.repository.ClientDataRepository;

@Service
public class ClientDataServiceImpl implements ClientDataService{

    @Autowired
    private ClientDataRepository clientDataRepository;
    @Override
    public ClientData create(ClientData clientData) {
        return clientDataRepository.save(clientData);
    }

    @Override
    public ClientData getByLogin(String login) {
        return clientDataRepository.findByLogin(login);
    }
}
