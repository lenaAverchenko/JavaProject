package telran.functionality.com.service;

import telran.functionality.com.entity.Agreement;

import java.util.List;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(long id);

    Agreement create(Agreement agreement);

    void changeStatus(long id, int status);

    void inactivateAgreement(long id);

    void changeInterestRate(long id, double newRate);

    void delete(long id);
}
