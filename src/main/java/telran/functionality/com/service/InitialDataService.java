package telran.functionality.com.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class InitialDataService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientDataRepository clientDataRepository;
    @Autowired
    private  ManagerDataRepository managerDataRepository;
    @Autowired
    private PasswordEncoder encoder;

    public void createManagerData() {
        managerRepository.save(new Manager("Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Dmytro", "Dmytreev", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
        managerRepository.save(new Manager("Antonina", "Anisimova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));
    }

    public void createClientData() {
        List<Manager> managers = managerRepository.findAll();
        clientRepository.save(new Client(managers.get(0), new ArrayList<>(), "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(1), new ArrayList<>(), "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(1), new ArrayList<>(), "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(2), new ArrayList<>(), "FT33333", "Evelina", "Evelinova", "eva@mail.com", "4/5 Upper Street", "444555666888", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(3), new ArrayList<>(), "FT44444", "Basia", "Bassnaja", "basia@mail.com", "34/8 TownDown Street", "555666444777", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(4), new ArrayList<>(), "FT55555", "Nikolaj", "Nikolaev", "nikola@mail.com", "4/99 Miles Street", "222999888777", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(4), new ArrayList<>(), "FT66666", "Vinsent", "Vinsentov", "vin@mail.com", "111 Good Street", "999666444222", new Timestamp(new Date().getTime())));
        clientRepository.save(new Client(managers.get(2), new ArrayList<>(), "FT77777", "Halina", "Halinova", "hala@mail.com", "99/3 Excellent Street", "666777444111", new Timestamp(new Date().getTime())));
    }

    public void createAccountData() {
        List<Client> clients = clientRepository.findAll();
        accountRepository.save(new Account(clients.get(0), "Bank Account", Type.INNERBANK, 0, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(1), "Personal Account", Type.PERSONAL, 1000, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(2), "Leo's Account", Type.PERSONAL, 200, Currency.USD, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(4), "Basia's Account", Type.PERSONAL, 0, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(3), "Flourish Company", Type.COMMERCIAL, 600, Currency.UAH, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(6), "Vinsent's Account", Type.PERSONAL, 3000, Currency.EUR, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(5), "Good company", Type.COMMERCIAL, 150, Currency.PLN, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(7), "Best company", Type.COMMERCIAL, 5000, Currency.EUR, new Timestamp(new Date().getTime())));
        accountRepository.save(new Account(clients.get(7), "Best company Personal", Type.COMMERCIAL, 0, Currency.USD, new Timestamp(new Date().getTime())));
    }

    public void createProductData() {
        List<Manager> managers = managerRepository.findAll();
        productRepository.save(new Product(managers.get(1), "credit", Currency.PLN, 7.00, 100000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(1), "mortgage", Currency.PLN, 3.00, 500000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(1), "classic", Currency.PLN, 1.00, 10000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(2), "credit", Currency.UAH, 15.00, 500000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(2), "mortgage", Currency.UAH, 20.00, 1000000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(2), "classic", Currency.UAH, 1.00, 50000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(3), "credit", Currency.USD, 5.00, 50000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(3), "mortgage", Currency.USD, 2.00, 300000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(3), "classic", Currency.USD, 1.00, 5000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(4), "credit", Currency.EUR, 6.00, 40000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(4), "mortgage", Currency.EUR, 2.50, 200000, new Timestamp(new Date().getTime())));
        productRepository.save(new Product(managers.get(4), "classic", Currency.EUR, 1.00, 8000, new Timestamp(new Date().getTime())));
    }

    public void createAgreementData() {
        List<Account> accounts = accountRepository.findAll();
        List<Product> products = productRepository.findAll();
        Account tempAccount = accounts.get(1);
        agreementRepository.save(new Agreement(tempAccount, products.get(1), 3.00, 100000, new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accounts.get(2);
        agreementRepository.save(new Agreement(tempAccount, products.get(3), 15.00, 500000, new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accounts.get(3);
        agreementRepository.save(new Agreement(tempAccount, products.get(6), 5.00, 50000, new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);

        tempAccount = accounts.get(4);
        agreementRepository.save(new Agreement(tempAccount, products.get(8), 1.00, 5000, new Timestamp(new Date().getTime())));
        tempAccount.setStatus(Status.ACTIVE);
        accountRepository.save(tempAccount);
    }

    public void createTransactionData() {
        List<Account> accounts = accountRepository.findAll();
        transactionRepository.save(new Transaction(accounts.get(1), accounts.get(2), Type.PERSONAL, 100, "Personal transfer"));
        transactionRepository.save(new Transaction(accounts.get(3), accounts.get(4), Type.COMMERCIAL, 1000, "Payment for service"));
        transactionRepository.save(new Transaction(accounts.get(4), accounts.get(2), Type.COMMERCIAL, 5000, "Salary payment"));
        transactionRepository.save(new Transaction(accounts.get(2), accounts.get(0), Type.INNERBANK, 100, "Withdrawal"));
        transactionRepository.save(new Transaction(accounts.get(1), accounts.get(4), Type.COMMERCIAL, 100, "For access to service"));
    }

    public void createUserInfoForClient(){
        List<Client> clients = clientRepository.findAll();
        clientDataRepository.save(new ClientData("First",encoder.encode("First"), clients.get(0).getId()));
        clientDataRepository.save(new ClientData("Second",encoder.encode("Second"), clients.get(1).getId()));
        clientDataRepository.save(new ClientData("Third",encoder.encode("Third"), clients.get(2).getId()));
        clientDataRepository.save(new ClientData("Fourth",encoder.encode("Fourth"), clients.get(3).getId()));
        clientDataRepository.save(new ClientData("Fifth",encoder.encode("Fifth"), clients.get(4).getId()));
        clientDataRepository.save(new ClientData("Sixth",encoder.encode("Sixth"), clients.get(5).getId()));
        clientDataRepository.save(new ClientData("Seventh",encoder.encode("Seventh"), clients.get(6).getId()));
        clientDataRepository.save(new ClientData("Eighth",encoder.encode("Eighth"), clients.get(7).getId()));

    }

    public void createUserInfoForManager(){
        List<Manager> managers = managerRepository.findAll();
        managerDataRepository.save(new ManagerData("ManagerOne", encoder.encode("ManagerOne"), managers.get(0).getId()));
        managerDataRepository.save(new ManagerData("ManagerTwo", encoder.encode("ManagerTwo"), managers.get(1).getId()));
        managerDataRepository.save(new ManagerData("ManagerThree", encoder.encode("ManagerThree"), managers.get(2).getId()));
        managerDataRepository.save(new ManagerData("ManagerFour", encoder.encode("ManagerFour"), managers.get(3).getId()));
        managerDataRepository.save(new ManagerData("ManagerFive", encoder.encode("ManagerFive"), managers.get(4).getId()));
    }

    public void createData() {
        createManagerData();
        createClientData();
        createAccountData();
        createProductData();
        createAgreementData();
        createTransactionData();
        createUserInfoForClient();
        createUserInfoForManager();
    }


}
