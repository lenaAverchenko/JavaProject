package telran.functionality.com.service;
/**
 * Interface ManagerDataService
 *
 * @author Olena Averchenko
 */

import telran.functionality.com.entity.ManagerData;

public interface ManagerDataService {

    ManagerData create(ManagerData managerData);

    ManagerData getByLogin(String login);
}
