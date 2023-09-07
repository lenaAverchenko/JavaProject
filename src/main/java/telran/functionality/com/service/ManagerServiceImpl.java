package telran.functionality.com.service;
/**
 * Class implementing ManagerService to manage information about Managers and their products
 * @see telran.functionality.com.service.ManagerService
 *
 * @author Olena Averchenko
 */
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    private final ProductService productService;

    private final ClientService clientService;

    /**
     * Method to get all the managers from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested managers
     */
    @Override
    public List<Manager> getAll() {
        List<Manager> allManagers = managerRepository.findAll();
        if (allManagers.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered manager");
        }
        return allManagers;
    }

    /**
     * Method to get the manager by its id
     * @param id unique id for the manager
     * @throws NotExistingEntityException if it doesn't exist
     * @return found manager
     */
    @Override
    public Manager getById(long id) {
        Manager foundManager = managerRepository.findById(id).orElse(null);
        if (foundManager == null) {
            throw new NotExistingEntityException(
                    String.format("Manager with id %d doesn't exist", id));
        }
        return foundManager;
    }

    /**
     * Method to save a new manager
     * @param manager new manager
     * @return saved manager
     */
    @Override
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    /**
     * Method to update existing manager
     * @param id unique id for the manager
     * @param manager updated manager information
     * @return updated manager saved in the database
     */
    @Override
    public Manager update(long id, Manager manager) {
        Manager currentManager = getById(id);
        currentManager.setFirstName(manager.getFirstName());
        currentManager.setLastName(manager.getLastName());
        currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
        managerRepository.save(currentManager);
        return currentManager;
    }

    /**
     * Method to delete manager by its id
     * @param id unique id for the manager
     */
    @Override
    @Transactional
    public void delete(long id) {
        Manager foundManager = getById(id);
        if (foundManager.getId() == 1) {
            throw new ForbiddenDeleteAttemptException("Unable to delete the primary manager.");
        }
        List<Client> clients = foundManager.getClients();
        List<Product> products = foundManager.getProducts();
        if (!clients.isEmpty()) {
            clients = clients.stream().map(client -> {
                client.setManager(managerRepository.getReferenceById(1L));
                return client;
            }).toList();
        }
        if (!products.isEmpty()) {
            products = products.stream().map(product -> {
                product.setManager(managerRepository.getReferenceById(1L));
                return product;
            }).toList();
        }
        managerRepository.delete(foundManager);
        for (Client client : clients) {
            clientService.save(client);
        }
        for (Product product : products) {
            productService.save(product);
        }
    }

    /**
     * Method to change status for the chosen product
     * @param managerId unique manager id
     * @param status new Status
     */
    @Override
    public void changeStatus(long managerId, Status status) {
        Manager manager = getById(managerId);
        manager.setStatus(status);
        manager.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(manager);
    }

    /**
     * Method to add some product to the existing manager
     * @param managerId unique manager id
     * @param product new product to add
     * @throws ConflictIdException if provided id params are not the same
     * @return with added product
     */
    @Override
    @Transactional
    public Manager addProduct(long managerId, Product product) {
//        long managerId = product.get
        Manager currentManager = getById(managerId);
        if (product.getManager().getId() != managerId) {
            throw new ConflictIdException("Provided id's are not the same. Check the data.");
        }
        if (statusIsValid(managerId)) {
            productService.save(product);
            currentManager.setUpdatedAt(new Timestamp(new Date().getTime()));
            managerRepository.save(currentManager);
        }
        return currentManager;
    }

    /**
     * Method to change status of product, if it belongs to the manager with provided id
     * @param managerId unique manager id
     * @param productId unique product id
     * @param status new status to replace the old one
     * @throws ProductDoesntBelongToManagerException if product belongs to any other manager
     */
    @Override
    public void changeStatusOfProduct(long managerId, long productId, Status status) {
        Product product = productService.getById(productId);
        if (!(product.getManager().getId() == managerId)) {
            throw new ProductDoesntBelongToManagerException(String.format("Product with id %d " +
                    "doesn't belong to manager with id %d", productId, managerId));
        }
        productService.changeStatus(productId, status);
    }

    /**
     * Method to change manager of the product
     * @param id unique product id
     * @param managerId unique manager id
     */
    @Override
    public void changeManagerOfProduct(long id, long managerId) {
        productService.changeManager(id, managerId);
    }

    /**
     * Method to change status of the product
     * @param id unique product id
     * @param status new status of chosen product
     */
    @Override
    public void changeStatusOfProduct(long id, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        productService.changeStatus(id, status);
    }

    /**
     * Method to change limit value for the product
     * @param id unique product id
     * @param limitValue a new limit for the chosen product
     * */
    @Override
    public void changeLimitValueOfProduct(long id, int limitValue) {
        productService.changeLimitValue(id, limitValue);
    }

    /**
     * Method to make a manager inactive
     * @param id unique manager id
     */
    @Override
    public void inactivateStatus(long id) {
        changeStatus(id, Status.INACTIVE);
    }

    /**
     * Method to check if the required manager exists
     * @param manager manager object to be checked
     * @throws NotExistingEntityException if manager doesn't exist
     * @return true, if the manager exists
     */
    public boolean managerExists(Manager manager) {
        if (manager == null) {
            throw new NotExistingEntityException("Manager doesn't exist");
        }
        return true;
    }

    /**
     * Method to check if the status of the chosen manager is active
     * @param id manager id
     * @throws InvalidStatusException if the manager has inactive status
     * @return true if the status of the manager is active
     */
    public boolean statusIsValid(long id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (managerExists(manager)) {
            if (manager.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the manager with id %d.", id));
            }
        }
        return true;
    }

    /**
     * Method to inactivate the chosen product by it's id
     * @param productId product id
     * @throws NotExistingEntityException if product doesn't exist
     */
    @Override
    public void inactivateStatusOfProduct(long productId) {
        productService.inactivateStatus(productId);
    }
}
