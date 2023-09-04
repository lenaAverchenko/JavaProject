package telran.functionality.com.service;

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

    @Override
    public ManagerData create(ManagerData managerData) {
        if (managerDataRepository.findAll().stream()
                .map(ManagerData::getLogin).toList()
                .contains(managerData.getLogin())){
            throw new ForbiddenLoginNameException(String.format("Manager with login %s is already exist.", managerData.getLogin()));
        }
        return managerDataRepository.save(managerData);
    }

    @Override
    public ManagerData getByLogin(String login) {
        ManagerData managerData = managerDataRepository.findByLogin(login);
        if (managerData == null){
            throw new UsernameNotFoundException("Manager with login " + login + " " + "not found");
        }
        return managerData;
    }
}
