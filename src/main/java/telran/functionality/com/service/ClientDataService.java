package telran.functionality.com.service;

import telran.functionality.com.entity.ClientData;

public interface ClientDataService {

    ClientData create(ClientData clientData);

    ClientData getByLogin(String login);
}
