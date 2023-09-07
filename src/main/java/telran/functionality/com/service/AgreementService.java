package telran.functionality.com.service;
/**
 * Interface AgreementService
 *
 * @author Olena Averchenko
 */
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.enums.Status;

import java.util.List;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(long id);

    Agreement save(Agreement agreement);

    Agreement changeStatus(long id, Status status);

    void inactivateAgreement(long id);

    Agreement changeInterestRate(long id, double newRate);

    void delete(long id);
}
