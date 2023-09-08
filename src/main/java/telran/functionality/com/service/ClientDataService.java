package telran.functionality.com.service;
/**
 * Interface ClientDataService
 *
 * @author Olena Averchenko
 */
import telran.functionality.com.entity.ClientData;

import java.util.Optional;

public interface ClientDataService {

    ClientData create(ClientData clientData);

    ClientData getByLogin(String login);
}
