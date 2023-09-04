package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.exceptions.ForbiddenLoginNameException;
import telran.functionality.com.repository.ClientDataRepository;

@Service
public class ClientDataServiceImpl implements ClientDataService{

    @Autowired
    private ClientDataRepository clientDataRepository;
    @Override
    public ClientData create(ClientData clientData) {
        if (clientDataRepository.findAll().stream()
                .map(ClientData::getLogin).toList()
                .contains(clientData.getLogin())){
            throw new ForbiddenLoginNameException(String.format("Client with login %s is already exist.", clientData.getLogin()));
        }
        return clientDataRepository.save(clientData);
    }

    @Override
    public ClientData getByLogin(String login) {
        ClientData clientData = clientDataRepository.findByLogin(login);
        if (clientData == null){
            throw new UsernameNotFoundException("Client with login " + login + " " + "not found");
        }
        return clientData;
    }
}
