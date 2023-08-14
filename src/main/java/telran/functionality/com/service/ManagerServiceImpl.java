package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<Manager> getAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getById(long id) {
        return managerRepository.findById(id).orElse(null);
    }

    @Override
    public Manager create(Manager manager) {
        return managerRepository.save(manager);
    }

     @Override
    public Manager update(long id, Manager manager) {
        List <Manager> managers = getAll();
        for (Manager currentManager: managers) {
            if (currentManager.getId() == id) {
                currentManager.setFirstName(manager.getFirstName());
                currentManager.setLastName(manager.getLastName());
                currentManager.setStatus(manager.getStatus());
                currentManager.setClients(manager.getClients());
                currentManager.setProducts(manager.getProducts());
                currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
                return currentManager;
            }
        }
        return null;
    }

    @Override
    public void delete(long id) {
        managerRepository.deleteById(id);
    }
}
