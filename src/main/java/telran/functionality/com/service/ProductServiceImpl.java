package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.ManagerRepository;
import telran.functionality.com.repository.ProductRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManagerRepository managerRepository;


    @Override
    public List<Product> getAll() {
        logger.info("Call method getAll products in service");
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered product");
        }
        return allProducts;
    }

    @Override
    public Product getById(long id) {
        logger.info("Call method getById {} product in service", id);
        Product foundProduct = productRepository.findById(id).orElse(null);
        if (foundProduct == null) {
            throw new NotExistingEntityException(
                    String.format("Product with id %d doesn't exist", id));
        }
        return foundProduct;
    }

    @Override
    public Product save(Product product) {
        logger.info("Call method save product in service");
        return productRepository.save(product);
    }

    @Override
    public void changeManager(long id, long managerId) {
        logger.info("Call method changeManager in service for product {}", id);
        if (statusIsValid(id)) {
            Product foundProduct = getById(id);
            Manager foundManager = managerRepository.findById(id).orElse(null);
            if (foundManager == null){
                throw new NotExistingEntityException(String.format("Manager with id %d does not exist", id));
            }
            if (foundManager.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Impossible to set manager with id %d to a product, " +
                        "because this manager is no longer available", managerId));
            }
            foundProduct.setManager(foundManager);
            Timestamp time = new Timestamp(new Date().getTime());
            foundProduct.setUpdatedAt(time);
            foundManager.setUpdatedAt(time);
            productRepository.save(foundProduct);
            managerRepository.save(foundManager);
        }
    }

    @Override
    public void changeStatus(long id, int status) {
        logger.info("Call method changeStatus in service for product {}", id);
        if (status < 0 || status > 2) {
            throw new WrongValueException("Status can only be: 0, 1, 2");
        }
        Product product = getById(id);
        product.setStatus(status);
        product.setUpdatedAt(new Timestamp(new Date().getTime()));
        productRepository.save(product);
    }

    @Override
    public void changeLimitValue(long id, int limitValue) {
        logger.info("Call method changeLimitValue in service for product {}", id);
        if (statusIsValid(id)) {
            Product product = getById(id);
            product.setLimitValue(limitValue);
            product.setUpdatedAt(new Timestamp(new Date().getTime()));
            save(product);
        }
    }


    @Override
    public void delete(long id) {
        logger.info("Call method delete product {} in service", id);
        Product foundProduct = getById(id);
        productRepository.delete(foundProduct);
    }

    @Override
    public void inactivateStatus(long id) {
        logger.info("Call method inactivateStatus in product service");
        changeStatus(id, 0);
    }

    public boolean productExists(Product product) {
        logger.info("Call method productExists in product service");
        if (product == null) {
            throw new NotExistingEntityException("Product doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(long id) {
        logger.info("Call method statusIsValid in product service");
        Product product = productRepository.findById(id).orElse(null);
        if (productExists(product)) {
            if (product.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Unable to get access to the product with id %d. Access denied", id));
            }
        }
        return true;
    }
}
