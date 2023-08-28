package telran.functionality.com.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class InitialDataService {
    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private  AgreementRepository agreementRepository;
    @Autowired
    private  ClientRepository clientRepository;
    @Autowired
    private  ManagerRepository managerRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  TransactionRepository transactionRepository;

    public void createManagerData(){
        managerRepository.save(new Manager("Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(),new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Dalim", "Dalimow",new ArrayList<>(), new ArrayList<>(),new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Anna", "Antonova",new ArrayList<>(), new ArrayList<>(),new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Dmytro", "Dmytreev",new ArrayList<>(), new ArrayList<>(),new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Antonina", "Anisimova",new ArrayList<>(), new ArrayList<>(),new Timestamp(new Date().getTime())));
    }

    public void createClientData(){
        clientRepository.save(new Client(managerRepository.findById(1L).orElse(null),new ArrayList<>(),"0000000","Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(1L).orElse(null),new ArrayList<>(),"FT11111","Michail","Michailov","michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(2L).orElse(null),new ArrayList<>(),"FT22222", "Leo","Leonov","leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(2L).orElse(null),new ArrayList<>(),"FT33333","Evelina","Evelinova","eva@mail.com", "4/5 Upper Street", "444555666888", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(3L).orElse(null),new ArrayList<>(),"FT44444","Basia","Bassnaja","basia@mail.com", "34/8 TownDown Street", "555666444777", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(4L).orElse(null),new ArrayList<>(),"FT55555","Nikolaj","Nikolaev","nikola@mail.com", "4/99 Miles Street", "222999888777", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(5L).orElse(null),new ArrayList<>(),"FT66666","Vinsent","Vinsentov","vin@mail.com", "111 Good Street", "999666444222", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managerRepository.findById(5L).orElse(null),new ArrayList<>(),"FT77777","Halina","Halinova","hala@mail.com", "99/3 Excellent Street", "666777444111", new Timestamp(new Date().getTime())));
    }

    public void createAccountData() {
        accountRepository.save(new Account(clientRepository.getReferenceById(1L), "Bank Account", Type.INNERBANK, 0, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(2L), "Personal Account", Type.PERSONAL, 1000, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(3L), "Leo's Account", Type.PERSONAL, 200, Currency.USD, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(5L), "Basia's Account", Type.PERSONAL, 0, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(4L), "Flourish Company", Type.COMMERCIAL, 600, Currency.UAH, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(7L), "Vinsent's Account", Type.PERSONAL, 3000, Currency.EUR, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(6L), "Good company", Type.COMMERCIAL, 150, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(8L), "Best company", Type.COMMERCIAL, 5000, Currency.EUR, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clientRepository.getReferenceById(8L), "Best company Personal", Type.COMMERCIAL, 0, Currency.USD, new Timestamp(new Date().getTime())));
    }

    public void createProductData(){
        productRepository.save(new Product(managerRepository.getReferenceById(2L),"credit", Currency.PLN, 7.00,100000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(2L),"mortgage", Currency.PLN, 3.00,500000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(2L),"classic", Currency.PLN, 1.00,10000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(3L),"credit", Currency.UAH, 15.00,500000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(3L),"mortgage", Currency.UAH, 20.00,1000000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(3L),"classic", Currency.UAH, 1.00,50000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(4L),"credit", Currency.USD, 5.00,50000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(4L),"mortgage", Currency.USD, 2.00,300000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(4L),"classic", Currency.USD, 1.00,5000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(5L),"credit", Currency.EUR, 6.00,40000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(5L),"mortgage", Currency.EUR, 2.50,200000,new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managerRepository.getReferenceById(5L),"classic", Currency.EUR, 1.00,8000,new Timestamp(new Date().getTime())));
    }

    public void createAgreementData(){
        Account tempAccount = accountRepository.getReferenceById(2L);
        agreementRepository.save(new Agreement(accountRepository.getReferenceById(2L),productRepository.getReferenceById(2L),3.00, 100000,new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accountRepository.getReferenceById(3L);
        agreementRepository.save(new Agreement(tempAccount,productRepository.getReferenceById(4L),15.00, 500000,new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accountRepository.getReferenceById(4L);
        agreementRepository.save(new Agreement(tempAccount,productRepository.getReferenceById(7L),5.00, 50000,new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accountRepository.getReferenceById(5L);
        agreementRepository.save(new Agreement(tempAccount,productRepository.getReferenceById(9L),1.00, 5000,new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);
    }
    public void createTransactionData(){
        transactionRepository.save(new Transaction(accountRepository.getReferenceById(2L), accountRepository.getReferenceById(3L),Type.PERSONAL, 100, "Personal transfer"));
        transactionRepository.save(new Transaction(accountRepository.getReferenceById(4L), accountRepository.getReferenceById(5L),Type.COMMERCIAL, 1000, "Payment for service"));
        transactionRepository.save(new Transaction(accountRepository.getReferenceById(5L), accountRepository.getReferenceById(2L),Type.COMMERCIAL, 5000, "Salary payment"));
        transactionRepository.save(new Transaction(accountRepository.getReferenceById(3L), accountRepository.getReferenceById(1L),Type.INNERBANK, 100, "Withdrawal"));
        transactionRepository.save(new Transaction(accountRepository.getReferenceById(2L), accountRepository.getReferenceById(5L),Type.COMMERCIAL, 100, "For access to service"));
    }

    public void createData(){
        createManagerData();
        createClientData();
        createAccountData();
        createProductData();
        createAgreementData();
        createTransactionData();
    }






}
