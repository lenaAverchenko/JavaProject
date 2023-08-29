package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.AgreementRepository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AgreementServiceImpl implements AgreementService {


    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public List<Agreement> getAll() {
        List<Agreement> allAgreements = agreementRepository.findAll();
        if (allAgreements.isEmpty()) {
            throw new EmptyRequiredListException("There is no one registered agreement");
        }
        return allAgreements;

    }

    @Override
    public Agreement getById(long id) {
        Agreement foundAgreement = agreementRepository.findById(id).orElse(null);
        if (foundAgreement == null) {
            throw new NotExistingEntityException(
                    String.format("Agreement with id %d doesn't exist", id));
        }
        return foundAgreement;
    }

    @Override
    public Agreement save(Agreement agreement) {
        Agreement createdAgreement = agreementRepository.save(agreement);
        UUID accountIban = createdAgreement.getAccount().getId();
        accountService.changeStatus(accountIban, Status.ACTIVE);
        return createdAgreement;
    }

    @Override
    public void changeStatus(long id, Status status) {
        if (!Arrays.stream(Status.values()).toList().contains(status)) {
            throw new WrongValueException("Status hasn't been recognized. Provided value is incorrect.");
        }
        Agreement agreement = getById(id);
        agreement.setStatus(status);
        agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(agreement);
    }


    @Override
    public void changeInterestRate(long id, double newRate) {
        Agreement agreement = getById(id);
        if (statusIsValid(id)) {
            agreement.setInterestRate(newRate);
            agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
            save(agreement);
        }
    }


    @Override
    public void delete(long id) {
        Agreement agreement = getById(id);
        agreementRepository.delete(agreement);
    }

    @Override
    public void inactivateAgreement(long id) {
        changeStatus(id, Status.INACTIVE);
        accountService.inactivateAccount(getById(id).getAccount().getId());
    }


    public boolean agreementExists(Agreement agreement) {
        if (agreement == null) {
            throw new NotExistingEntityException("Agreement doesn't exist");
        }
        return true;
    }

    public boolean statusIsValid(long id) {
        Agreement agreement = agreementRepository.findById(id).orElse(null);
        if (agreementExists(agreement)) {
            if (agreement.getStatus().equals(Status.INACTIVE)) {
                throw new InvalidStatusException(String.format("Unable to get access to the agreement with id %d.", id));
            }
        }
        return true;
    }
}
