package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.repository.ManagerDataRepository;

@Service
public class ManagerDataServiceImpl implements ManagerDataService {

    @Autowired
    private ManagerDataRepository managerDataRepository;

    @Override
    public ManagerData create(ManagerData managerData) {
        return managerDataRepository.save(managerData);
    }

    @Override
    public ManagerData getByLogin(String login) {
        return managerDataRepository.findByLogin(login);
    }
}
