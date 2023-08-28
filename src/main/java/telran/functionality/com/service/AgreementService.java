package telran.functionality.com.service;

import telran.functionality.com.entity.Agreement;
import telran.functionality.com.enums.Status;

import java.util.List;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(long id);

    Agreement save(Agreement agreement);

    void changeStatus(long id, Status status);

    void inactivateAgreement(long id);

    void changeInterestRate(long id, double newRate);

    void delete(long id);
}
