package telran.functionality.com.service;

import telran.functionality.com.entity.Agreement;

import java.util.List;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(long id);

    Agreement create(Agreement account);

    Agreement update(long id);

    void delete(long id);
}
