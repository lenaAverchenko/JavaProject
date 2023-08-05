package telran.functionality.com.service;

import telran.functionality.com.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<Account> getAll();

    Account getById(UUID id);

    Account create(Account account);

    Account update(UUID id);

    void delete(UUID id);


}
