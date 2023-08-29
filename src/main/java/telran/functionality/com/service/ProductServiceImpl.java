package telran.functionality.com.service;


import org.springframework.beans.factory.annotation.Autowired;
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

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManagerRepository managerRepository;


    @Override
    public List<Product> getAll() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered product");
        }
        return allProducts;
    }

    @Override
    public Product getById(long id) {
        Product foundProduct = productRepository.findById(id).orElse(null);
        if (foundProduct == null) {
            throw new NotExistingEntityException(
                    String.format("Product with id %d doesn't exist", id));
        }
        return foundProduct;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
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
            foundProduct.setManager(foundManager);
            Timestamp time = new Timestamp(new Date().getTime());
            foundProduct.setUpdatedAt(time);
            foundManager.setUpdatedAt(time);
            productRepository.save(foundProduct);
            managerRepository.save(foundManager);
        }
    }

    @Override
    public void changeStatus(long id, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        Product product = getById(id);
        product.setStatus(status);
        product.setUpdatedAt(new Timestamp(new Date().getTime()));
        productRepository.save(product);
    }

    @Override
    public void changeLimitValue(long id, int limitValue) {
        if (statusIsValid(id)) {
            Product product = getById(id);
            product.setLimitValue(limitValue);
            product.setUpdatedAt(new Timestamp(new Date().getTime()));
            save(product);
        }
    }


    @Override
    public void delete(long id) {
        Product foundProduct = getById(id);
        productRepository.delete(foundProduct);
    }

    @Override
    public void inactivateStatus(long id) {
        changeStatus(id, Status.INACTIVE);
    }

    public boolean productExists(Product product) {
        if (product == null) {
            throw new NotExistingEntityException("Product doesn't exist");
        }
        return true;
    }

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
