package telran.functionality.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.AgreementRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AgreementServiceImpl implements AgreementService {

    private static final Logger logger = LoggerFactory.getLogger(AgreementServiceImpl.class);

    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public List<Agreement> getAll() {
        logger.info("Call method getAll agreements in service");
        List<Agreement> allAgreements = agreementRepository.findAll();
        if (allAgreements.size() == 0) {
            throw new EmptyRequiredListException("There is no one registered agreement");
        }
        return allAgreements;

    }

    @Override
    public Agreement getById(long id) {
        logger.info("Call method getById {} agreement in service", id);
        Agreement foundAgreement = agreementRepository.findById(id).orElse(null);
        if (foundAgreement == null) {
            throw new NotExistingEntityException(
                    String.format("Agreement with id %d doesn't exist", id));
        }
        return foundAgreement;
    }

    @Override
    public Agreement create(Agreement agreement) {
        logger.info("Call method create agreement in service");
        Agreement createdAgreement = agreementRepository.save(agreement);
        UUID accountId = createdAgreement.getAccount().getId();
        accountService.changeStatus(accountId, 1);
        return createdAgreement;
    }

    @Override
    public void changeStatus(long id, int status) {
        logger.info("Call method changeStatus for agreement in service with id: {} from {} to {}", id, getById(id).getStatus(), status);
        if (status < 0 || status > 2) {
            throw new WrongValueException("Status can only be: 0, 1, 2");
        }
        Agreement agreement = getById(id);
        agreement.setStatus(status);
        agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
        create(agreement);
    }


    @Override
    public void changeInterestRate(long id, double newRate) {
        logger.info("Call method changeInterestRate for agreement in service with id: {}", id);
        Agreement agreement = getById(id);
        if (statusIsValid(id)) {
            agreement.setInterestRate(newRate);
            agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
            create(agreement);
        }
    }


    @Override
    public void delete(long id) {
        logger.info("Call method delete agreement in service by id: {}", id);
        Agreement agreement = getById(id);
        agreementRepository.delete(agreement);
    }

    @Override
    public void inactivateAgreement(long id) {
        logger.info("Call method inactivateAgreement in agreement service");
        changeStatus(id, 0);
    }


    public boolean agreementExists(Agreement agreement) {
        logger.info("Call method agreementExists in agreement service");
        if (agreement == null) {
            throw new NotExistingEntityException("Agreement doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(long id) {
        logger.info("Call method statusIsValid in agreement service");
        Agreement agreement = agreementRepository.findById(id).orElse(null);
        if (agreementExists(agreement)) {
            if (agreement.getStatus() == 0) {
                throw new InvalidStatusException(String.format("Unable to get access to the agreement with id %d.", id));
            }
        }
        return true;
    }
}
