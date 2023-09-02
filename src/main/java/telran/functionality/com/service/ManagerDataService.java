package telran.functionality.com.service;

import telran.functionality.com.entity.ManagerData;

public interface ManagerDataService {

    ManagerData create(ManagerData managerData);

    ManagerData getByLogin(String login);
}
