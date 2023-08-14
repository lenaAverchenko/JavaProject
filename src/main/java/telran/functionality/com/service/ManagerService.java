package telran.functionality.com.service;

import telran.functionality.com.entity.Manager;

import java.util.List;


public interface ManagerService {

    List<Manager> getAll();

    Manager getById(long id);

    Manager create(Manager account);

    Manager update(long id, Manager manager);

    void delete(long id);
}
