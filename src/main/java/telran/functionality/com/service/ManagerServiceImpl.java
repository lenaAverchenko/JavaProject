package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class ManagerServiceImpl implements ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProductService productService;

    @Override
    public List<Manager> getAll() {
        logger.info("Call method getAll managers in service");
        List<Manager> allManagers = managerRepository.findAll();
        if (allManagers.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered manager");
        }
        return allManagers;
    }

    @Override
    public Manager getById(long id) {
        logger.info("Call method getById {} manager in service", id);
        Manager foundManager = managerRepository.findById(id).orElse(null);
        if (foundManager == null) {
            throw new NotExistingEntityException(
                    String.format("Manager with id %d doesn't exist", id));
        }
        return foundManager;
    }

    @Override
    public Manager create(Manager manager) {
        logger.info("Call method create manager in service");
        return managerRepository.save(manager);
    }

    @Override
    public Manager update(long id, Manager manager) {
        logger.info("Call method update {} manager in service", id);
        Manager currentManager = getById(id);
        currentManager.setFirstName(manager.getFirstName());
        currentManager.setLastName(manager.getLastName());
        currentManager.setStatus(manager.getStatus());
        currentManager.setClients(manager.getClients());
        currentManager.setProducts(manager.getProducts());
        currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
        managerRepository.save(currentManager);
        return currentManager;
    }

    @Override
    public void delete(long id) {
        logger.info("Call method delete {} manager in service", id);
        Manager foundManager = getById(id);
        managerRepository.delete(foundManager);
    }

    @Override
    public void changeStatus(long managerId, int status) {
        logger.info("Call method changeStatus of manager with id: {} in service from {} to {}", managerId, getById(managerId).getStatus(), status);
        Manager manager = getById(managerId);
        manager.setStatus(status);
        manager.setUpdatedAt(new Timestamp(new Date().getTime()));
        create(manager);
    }

    @Override
    public Manager addProduct(long managerId, Product product) {
        logger.info("Call method addProduct to manager with id: {} in service", managerId);
        Manager currentManager = getById(managerId);
        if (statusIsValid(managerId)) {
            currentManager.getProducts().add(product);
            currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
            managerRepository.save(currentManager);
        }
        return currentManager;
    }

    @Override
    public void changeStatusOfProduct(long managerId, long productId, int status) {
        logger.info("Call method changeStatusOfProduct {} to manager with id: {}", productId, managerId);
        Product product = productService.getById(productId);
        if (!(product.getManager().getId() == managerId)) {
            throw new ProductDoesntBelongToManagerException(String.format("Product with id %d " +
                    "doesn't belong to manager with id %d", productId, managerId));
        }
        productService.changeStatus(productId, status);
    }

    @Override
    public void changeManagerOfProduct(long id, long managerId) {
        logger.info("Call method changeManagerOfProduct in manager service");
        productService.changeManager(id, managerId);
    }

    @Override
    public void changeStatusOfProduct(long id, int status) {
        logger.info("Call method changeStatusOfProduct in manager service");
        if (status < 0 || status > 2) {
            throw new WrongValueException("Status can only be: 0, 1, 2");
        }
        productService.changeStatus(id, status);
    }

    @Override
    public void changeLimitValueOfProduct(long id, int limitValue) {
        logger.info("Call method changeLimitValueOfProduct in manager service");
        productService.changeLimitValue(id, limitValue);
    }

    @Override
    public void inactivateStatus(long id) {
        logger.info("Call method inactivateStatus in manager service");
        changeStatus(id, 0);
    }

    public boolean managerExists(Manager manager) {
        logger.info("Call method productExists in manager service");
        if (manager == null) {
            throw new NotExistingEntityException("Manager doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(long id) {
        logger.info("Call method statusIsValid in manager service");
        Manager manager = managerRepository.findById(id).orElse(null);
        if (managerExists(manager)) {
            if (manager.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Unable to get access to the manager with id %d.", id));
            }
        }
        return true;
    }

    @Override
    public void inactivateStatusOfProduct(long productId) {
        logger.info("Call method inactivateStatusOfProduct in manager service");
        productService.inactivateStatus(productId);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Manager manager = getById(productService.getById(productId).getManager().getId());
        manager.setUpdatedAt(timestamp);
        create(manager);
    }
}
