package telran.functionality.com.service;
/**
 * Class implementing AgreementService to manage information of agreements
 *
 * @author Olena Averchenko
 * @see telran.functionality.com.service.AgreementService
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.*;
import telran.functionality.com.repository.AgreementRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;

    private final AccountService accountService;

    /**
     * Method to get all the agreements from database
     * @throws EmptyRequiredListException if the returned is empty
     * @return list of requested agreements
     */
    @Override
    public List<Agreement> getAll() {
        List<Agreement> allAgreements = agreementRepository.findAll();
        if (allAgreements.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered agreement");
        }
        return allAgreements;

    }

    /**
     * Method to get the agreement by its id
     * @param id unique id for the agreement
     * @throws NotExistingEntityException if it doesn't exist
     * @return found agreement
     */
    @Override
    public Agreement getById(long id) {
        return agreementRepository.findById(id).orElseThrow(() -> new NotExistingEntityException(
                String.format("Agreement with id %d doesn't exist", id)));
    }

    /**
     * Method to save a new agreement
     * @param agreement new agreement
     * @return Agreement saved agreement
     */
    @Override
    @Transactional
    public Agreement save(Agreement agreement) {
        Agreement createdAgreement = agreementRepository.save(agreement);
        UUID accountId = createdAgreement.getAccount().getId();
        accountService.changeStatus(accountId, Status.ACTIVE);
        return createdAgreement;
    }

    /**
     * Method to change status for the chosen agreement
     * @param id unique agreement id
     * @param status new Status
     * @return agreement with changed status
     */
    @Override
    public Agreement changeStatus(long id, Status status) {
        Agreement agreement = getById(id);
        agreement.setStatus(status);
        agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
        return save(agreement);
    }

    /**
     * Method to change the interest rate for the chosen agreement
     * @param id unique agreement id
     * @param newRate new value of rate
     * @return agreement with changed value of the interest rate
     */
    @Override
    public Agreement changeInterestRate(long id, double newRate) {
        Agreement agreement = getById(id);
        if (!statusIsValid(id)) {
            throw new InvalidStatusException(String.format("Unable to get access to the agreement with id %d.", id));
        }
        agreement.setInterestRate(newRate);
        agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
        return save(agreement);
    }

    /**
     * Method to delete agreement by its id
     * @param id unique id for the agreement
     */
    @Override
    public void delete(long id) {
        Agreement agreement = getById(id);
        agreementRepository.delete(agreement);
    }

    /**
     * Method to make the agreement inactive
     * @param id unique agreement id
     */
    @Override
    @Transactional
    public void inactivateAgreement(long id) {
        changeStatus(id, Status.INACTIVE);
        accountService.inactivateAccount(getById(id).getAccount().getId());
    }

    /**
     * Method to check if the required agreement exists
     * @param agreement agreement object to be checked
     * @throws NotExistingEntityException if agreement doesn't exist
     * @return true, if the agreement exists
     */
    public boolean agreementExists(Agreement agreement) {
        if (agreement == null) {
            throw new NotExistingEntityException("Agreement doesn't exist");
        }
        return true;
    }

    /**
     * Method to check if the status of the chosen agreement is active
     * @param id agreement id
     * @throws InvalidStatusException if the agreement has an inactive status
     * @return true if the agreement has an active status
     */
    public boolean statusIsValid(long id) {
        Agreement agreement = getById(id);
        return Status.ACTIVE.equals(agreement.getStatus());
    }
}