package telran.functionality.com.service;

import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(long id);

    Product save(Product product);

    void changeManager(long id, long managerId);

    void changeStatus(long id, Status status);

    void changeLimitValue(long id, int limitValue);

    void delete(long id);

    void inactivateStatus(long id);
}