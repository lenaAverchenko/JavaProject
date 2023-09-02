package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import telran.functionality.com.service.InitialDataService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestDataController {

    private final InitialDataService initialDataService;

    @PostMapping("/createData")
    public void createTestData() {
        initialDataService.createData();
    }

    @PostMapping("/createManagers")
    public void createTestManagerData() {
        initialDataService.createManagerData();
    }

    @PostMapping("/createClients")
    public void createTestClientData() {
        initialDataService.createClientData();
    }

    @PostMapping("/createAccounts")
    public void createTestAccountData() {
        initialDataService.createAccountData();
    }

    @PostMapping("/createProducts")
    public void createTestProductData() {
        initialDataService.createProductData();
    }

    @PostMapping("/createAgreements")
    public void createTestAgreementData() {
        initialDataService.createAgreementData();
    }

    @PostMapping("/createTransactions")
    public void createTestTransactionData() {
        initialDataService.createTransactionData();
    }

    @PostMapping("/createUserInfoForClient")
    public void createUserInfoForClient() {
        initialDataService.createUserInfoForClient();
    }

    @PostMapping("/createUserInfoForManager")
    public void createUserInfoForManager() {
        initialDataService.createUserInfoForManager();
    }
}
