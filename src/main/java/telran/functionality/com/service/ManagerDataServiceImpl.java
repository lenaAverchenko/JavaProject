package telran.functionality.com.service;
/**
 * Class implementing ManagerDataService to manage information about Managers' access to the App
 *
 * @author Olena Averchenko
 * @see telran.functionality.com.service.ManagerDataService
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.exceptions.ForbiddenLoginNameException;
import telran.functionality.com.repository.ManagerDataRepository;

@Service
public class ManagerDataServiceImpl implements ManagerDataService {

    @Autowired
    private ManagerDataRepository managerDataRepository;

    /**
     * Method to create ManagerData entity
     * @param managerData the entity to save
     * @throws ForbiddenLoginNameException if the login, which user is trying to save already exists
     * @return ManagerData object, saved in database
     */
    @Override
    public ManagerData create(ManagerData managerData) {
        if (managerDataRepository.findAll().stream()
                .map(ManagerData::getLogin).toList()
                .contains(managerData.getLogin())) {
            throw new ForbiddenLoginNameException(String.format("Manager with login %s already exists.", managerData.getLogin()));
        }
        return managerDataRepository.save(managerData);
    }

    /**
     * Method to get ManagerData object by the login, stored in datrabase
     * @param login provided login
     * @throws UsernameNotFoundException if the manager with provided login hasn't been found
     * @return ManagerData object
     */
    @Override
    public ManagerData getByLogin(String login) {
        return managerDataRepository.findByLogin(login);
    }
}
