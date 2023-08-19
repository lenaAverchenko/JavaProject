package telran.functionality.com.service;

import telran.functionality.com.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(long id);

    Product save(Product account);

    void changeManager(long id, long managerId);

    void changeStatus(long id, int status);

    void changeLimitValue(long id, int limitValue);

    void delete(long id);

    void inactivateStatus(long id);

}
