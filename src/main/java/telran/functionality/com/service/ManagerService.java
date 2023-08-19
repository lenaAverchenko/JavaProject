package telran.functionality.com.service;

import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;

import java.util.List;


public interface ManagerService {

    List<Manager> getAll();

    Manager getById(long id);

    Manager create(Manager account);

    Manager update(long id, Manager manager);

    void delete(long id);

    void changeStatus(long managerId, int status);

    Manager addProduct(long managerId, Product product);

    void changeStatusOfProduct(long managerId, long productId, int status);

    void changeManagerOfProduct(long id, long managerId);

    void changeStatusOfProduct(long id, int status);

    void changeLimitValueOfProduct(long id, int limitValue);

    void inactivateStatus(long id);

    void inactivateStatusOfProduct(long productId);
}