package telran.functionality.com.service;

import telran.functionality.com.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(long id);

    Product create(Product account);

    Product update(long id);

    void delete(long id);

}
