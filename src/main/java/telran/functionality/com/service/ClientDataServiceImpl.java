package telran.functionality.com.service;
/**
 * Class implementing ClientDataService to manage information about Clients' access to the App
 *
 * @author Olena Averchenko
 * @see telran.functionality.com.service.ClientDataService
 */

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.exceptions.ForbiddenLoginNameException;
import telran.functionality.com.repository.ClientDataRepository;

@Service
@RequiredArgsConstructor
public class ClientDataServiceImpl implements ClientDataService {

    private final ClientDataRepository clientDataRepository;

    /**
     * Method to create ClientData entity
     * @param clientData the entity to save
     * @throws ForbiddenLoginNameException if the login, which user is trying to save already exists
     * @return ClientData object, saved in database
     */
    @Override
    public ClientData create(ClientData clientData) {
        if (clientDataRepository.findAll().stream()
                .map(ClientData::getLogin).toList()
                .contains(clientData.getLogin())) {
            throw new ForbiddenLoginNameException(String.format("Client with login %s already exists.", clientData.getLogin()));
        }
        return clientDataRepository.save(clientData);
    }

    /**
     * Method to get ClientData object by the login, stored in database
     * @param login provided login
     * @throws UsernameNotFoundException if the client with provided login hasn't been found
     * @return ClientData object
     */
    @Override
    public ClientData getByLogin(String login) {
        return clientDataRepository.findByLogin(login);
    }
}
