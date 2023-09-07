package telran.functionality.com.service;
/**
 * Interface ManagerService
 *
 * @author Olena Averchenko
 */
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;

import java.util.List;


public interface ManagerService {

    List<Manager> getAll();

    Manager getById(long id);

    Manager save(Manager manager);

    Manager update(long id, Manager manager);

    void delete(long id);

    void changeStatus(long managerId, Status status);

    Manager addProduct(long managerId, Product product);

    void changeStatusOfProduct(long managerId, long productId, Status status);

    void changeManagerOfProduct(long id, long managerId);

    void changeStatusOfProduct(long id, Status status);

    void changeLimitValueOfProduct(long id, int limitValue);

    void inactivateStatus(long id);

    void inactivateStatusOfProduct(long productId);
}