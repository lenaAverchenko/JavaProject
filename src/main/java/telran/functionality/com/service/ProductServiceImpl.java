package telran.functionality.com.service;
/**
 * Class implementing ProductService to manage information for products
 * @see telran.functionality.com.service.ProductService
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.ManagerRepository;
import telran.functionality.com.repository.ProductRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ManagerRepository managerRepository;

    /**
     * Method to get all the products from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested products
     */
    @Override
    public List<Product> getAll() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered product");
        }
        return allProducts;
    }
    /**
     * Method to get the product by its id
     * @throws NotExistingEntityException if it doesn't exist
     * @param id unique id for the product
     * @return found product
     */
    @Override
    public Product getById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotExistingEntityException(String.format("Product with id %d doesn't exist", id)));
    }

    /**
     * Method to save a new product
     * @param product new product
     * @return saved product
     */
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Method to change manager for the chosen product
     * @param id product id
     * @param managerId id of the new manager to the product
     * @throws NotExistingEntityException if this new manager doesn't exist
     * @throws InvalidStatusException if new Manager is not active
     */
    @Override
    @Transactional
    public void changeManager(long id, long managerId) {
        if (statusIsValid(id)) {
            Product foundProduct = getById(id);
            Manager foundManager = managerRepository.findById(managerId).orElse(null);
            if (foundManager == null) {
                throw new NotExistingEntityException(String.format("Manager with id %d does not exist", managerId));
            }
            if (foundManager.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Impossible to set manager with id %d to a product, " +
                        "because this manager is no longer available", managerId));
            }
            Timestamp time = new Timestamp(new Date().getTime());
            foundProduct.setUpdatedAt(time);
            foundManager.setUpdatedAt(time);
            foundProduct.setManager(foundManager);
            productRepository.save(foundProduct);
//            managerRepository.save(foundManager);
        }
    }

    /**
     * Method to change status for the chosen product
     * @param id product id
     * @param status new Status
     */
    @Override
    public void changeStatus(long id, Status status) {
        Product product = getById(id);
        product.setStatus(status);
        product.setUpdatedAt(new Timestamp(new Date().getTime()));
        productRepository.save(product);
    }

    /**
     * Method to change limit for the chosen product
     * @param id product id
     * @param limitValue new limit
     */
    @Override
    public void changeLimitValue(long id, int limitValue) {
        if (statusIsValid(id)) {
            Product product = getById(id);
            product.setLimitValue(limitValue);
            product.setUpdatedAt(new Timestamp(new Date().getTime()));
            save(product);
        }
    }

    /**
     * Method to delete product by its id
     * @param id unique id for the product
     */
    @Override
    public void delete(long id) {
        Product foundProduct = getById(id);
        productRepository.delete(foundProduct);
    }

    /**
     * Method to inactivate the chosen product
     * @param id product id
     * @throws NotExistingEntityException if product doesn't exist
     */
    @Override
    public void inactivateStatus(long id) {
        changeStatus(id, Status.INACTIVE);
    }

    /**
     * Method to check if the chosen product exists
     * @param product product
     * @throws NotExistingEntityException if product doesn't exist
     */
    public boolean productExists(Product product) {
        if (product == null) {
            throw new NotExistingEntityException("Product doesn't exist");
        }
        return true;
    }

    /**
     * Method to check if the status of the chosen product is valid
     * @param id product id
     * @throws InvalidStatusException if the product has inactive status
     * @return true if status is active
     */
    public boolean statusIsValid(long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (productExists(product)) {
            if (product.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the product with id %d. Access denied", id));
            }
        }
        return true;
    }
}
