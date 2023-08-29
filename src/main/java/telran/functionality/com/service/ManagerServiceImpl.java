package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.ManagerRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class ManagerServiceImpl implements ManagerService {


    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ClientService clientService;

    @Override
    public List<Manager> getAll() {
        List<Manager> allManagers = managerRepository.findAll();
        if (allManagers.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered manager");
        }
        return allManagers;
    }

    @Override
    public Manager getById(long id) {
        Manager foundManager = managerRepository.findById(id).orElse(null);
        if (foundManager == null) {
            throw new NotExistingEntityException(
                    String.format("Manager with id %d doesn't exist", id));
        }
        return foundManager;
    }

    @Override
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager update(long id, Manager manager) {
        Manager currentManager = getById(id);
        currentManager.setFirstName(manager.getFirstName());
        currentManager.setLastName(manager.getLastName());
        currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
        managerRepository.save(currentManager);
        return currentManager;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Manager foundManager = getById(id);
        if (foundManager.getId() == 1){
            throw new ForbiddenDeleteAttemptException("Unable to delete the primary manager.");
        }
        List<Client> clients = foundManager.getClients();
        List<Product> products = foundManager.getProducts();
        if (!clients.isEmpty()){
            clients = clients.stream().map(client -> {
                client.setManager(managerRepository.getReferenceById(1L));
            return client;}).toList();
        }
        if (!products.isEmpty()){
            products = products.stream().map(product -> {
                product.setManager(managerRepository.getReferenceById(1L));
                return product;}).toList();
        }
        managerRepository.delete(foundManager);
        for (Client client : clients) {
            clientService.save(client);
        }
        for (Product product : products) {
            productService.save(product);
        }
    }

    @Override
    public void changeStatus(long managerId, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        Manager manager = getById(managerId);
        manager.setStatus(status);
        manager.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(manager);
    }

    @Override
    public Manager addProduct(long managerId, Product product) {
        Manager currentManager = getById(managerId);
        if(product.getManager().getId() != managerId){
            throw new ConflictIdException("Provided id's are not the same. Check the data.");
        }
        if (statusIsValid(managerId)) {
            productService.save(product);
            currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
            managerRepository.save(currentManager);
        }
        return currentManager;
    }

    @Override
    public void changeStatusOfProduct(long managerId, long productId, Status status) {
        Product product = productService.getById(productId);
        if (!(product.getManager().getId() == managerId)) {
            throw new ProductDoesntBelongToManagerException(String.format("Product with id %d " +
                    "doesn't belong to manager with id %d", productId, managerId));
        }
        productService.changeStatus(productId, status);
    }

    @Override
    public void changeManagerOfProduct(long id, long managerId) {
        productService.changeManager(id, managerId);
    }

    @Override
    public void changeStatusOfProduct(long id, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        productService.changeStatus(id, status);
    }

    @Override
    public void changeLimitValueOfProduct(long id, int limitValue) {
        productService.changeLimitValue(id, limitValue);
    }

    @Override
    public void inactivateStatus(long id) {
        changeStatus(id, Status.INACTIVE);
    }

    public boolean managerExists(Manager manager) {
        if (manager == null) {
            throw new NotExistingEntityException("Manager doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(long id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (managerExists(manager)) {
            if (manager.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the manager with id %d.", id));
            }
        }
        return true;
    }

    @Override
    public void inactivateStatusOfProduct(long productId) {
        productService.inactivateStatus(productId);
    }
}
