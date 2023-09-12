package telran.functionality.com.controller;
/**
 * Class TestDataController - Rest controller, to create test data
 *
 * @author Olena Averchenko
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import telran.functionality.com.service.InitialDataService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "Test Data Controller", description = "Test Data Generation Controller")
public class TestDataController {

    private final InitialDataService initialDataService;


    @PostMapping("/createData")
    @Operation(summary = "Create test data", description = "Create all needed data for tests")
    public void createTestData() {
        initialDataService.createData();
    }


    @PostMapping("/createManagers")
    @Operation(summary = "Create test manager data", description = "Create managers data for tests")
    public void createTestManagerData() {
        initialDataService.createManagerData();
    }


    @PostMapping("/createClients")
    @Operation(summary = "Create test client data", description = "Create clients data for tests")
    public void createTestClientData() {
        initialDataService.createClientData();
    }

    @PostMapping("/createAccounts")
    @Operation(summary = "Create test account data", description = "Create accounts data for tests")
    public void createTestAccountData() {
        initialDataService.createAccountData();
    }


    @PostMapping("/createProducts")
    @Operation(summary = "Create test product data", description = "Create products data for tests")
    public void createTestProductData() {
        initialDataService.createProductData();
    }


    @PostMapping("/createAgreements")
    @Operation(summary = "Create test agreement data", description = "Creating agreements data for tests")
    public void createTestAgreementData() {
        initialDataService.createAgreementData();
    }


    @PostMapping("/createTransactions")
    @Operation(summary = "Create test transaction data", description = "Creating transactions data for tests")
    public void createTestTransactionData() {
        initialDataService.createTransactionData();
    }

    @PostMapping("/createUserInfoForClient")
    @Operation(summary = "Create user info for client", description = "Creating access info data for clients for tests")
    public void createUserInfoForClient() {
        initialDataService.createUserInfoForClient();
    }

    @PostMapping("/createUserInfoForManager")
    @Operation(summary = "Create user info for manager", description = "Creating access info data for managers for tests")
    public void createUserInfoForManager() {
        initialDataService.createUserInfoForManager();
    }
}