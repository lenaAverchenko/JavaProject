package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.repository.AgreementRepository;

import java.util.List;

@Service
public class AgreementServiceImpl implements AgreementService {

    @Autowired
    private AgreementRepository agreementRepository;

    @Override
    public List<Agreement> getAll() {
        return agreementRepository.findAll();
    }

    @Override
    public Agreement getById(long id) {
        return agreementRepository.findById(id).orElse(null);
    }

    @Override
    public Agreement create(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    @Override
    public Agreement update(long id) {
        return null;
    }

    @Override
    public void delete(long id) {
        agreementRepository.deleteById(id);
    }
}
