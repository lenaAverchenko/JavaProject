package telran.functionality.com.service;
/**
 * Class implementing AgreementService to manage information of agreements
 * @see telran.functionality.com.service.AgreementService
 *
 * @author Olena Averchenko
 * */
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.InvalidStatusException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.exceptions.WrongValueException;
import telran.functionality.com.repository.AgreementRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
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
     * @return List<Agreement> list of requested agreements
     * */
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
        return agreementRepository.findById(id).orElseThrow(() -> new NotExistingEntityException(
                String.format("Agreement with id %d doesn't exist", id)));
    }

    @Override
    @Transactional
    public Agreement save(Agreement agreement) {
        Agreement createdAgreement = agreementRepository.save(agreement);
        UUID accountIban = createdAgreement.getAccount().getId();
        accountService.changeStatus(accountIban, Status.ACTIVE);
        return createdAgreement;
    }

    @Override
    public Agreement changeStatus(long id, Status status) {
        Agreement agreement = getById(id);
        agreement.setStatus(status);
        agreement.setUpdatedAt(new Timestamp(new Date().getTime()));
        return save(agreement);
    }


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

    @Override
    public void delete(long id) {
        Agreement agreement = getById(id);
        agreementRepository.delete(agreement);
    }

    @Override
    @Transactional
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
        Agreement agreement = getById(id);
        return Status.ACTIVE.equals(agreement.getStatus());
    }
}