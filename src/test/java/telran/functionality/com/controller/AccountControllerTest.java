package telran.functionality.com.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.AgreementService;
import telran.functionality.com.service.ClientDataService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private AgreementService agreementService;
    @MockBean
    private Converter<Account, AccountDto, AccountCreateDto> accountConverter;
    @MockBean
    private Converter<Agreement, AgreementDto, AgreementCreateDto> agreementConverter;
    @MockBean
    private Converter<Transaction, TransactionDto, TransactionCreateDto> transactionConverter;
    @MockBean
    private ClientDataService clientDataService;


    @Test
    void getAll() {
        Account account =  new Account(UUID.randomUUID(),
                new Client(UUID.randomUUID(),
                        new Manager(1, "Oleh", "Olehov", new ArrayList<>(),
                                new ArrayList<>(), new Timestamp(new Date().getTime())),
                        new ArrayList<>(), Status.ACTIVE, "0000000",
                        "Bank", "Bank", "bank@mail.com",
                        "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                "Bank Account", Type.INNERBANK,
                Status.ACTIVE, 0, Currency.PLN, new Timestamp(new Date().getTime()));
        Mockito.when(accountService.getAll()).thenReturn(Arrays.asList(account));

    }

    @Test
    void getAllAgreements() {
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void delete() {
    }

    @Test
    void getBalanceOf() {
    }

    @Test
    void getHistoryOfTransactionsByAccountId() {
    }

    @Test
    void transferMoneyBetweenAccounts() {
    }

    @Test
    void withdrawMoney() {
    }

    @Test
    void putMoney() {
    }

    @Test
    void createNewDeal() {
    }

    @Test
    void inactivateAccount() {
    }

    @Test
    void accountBelongsToClient() {
    }

    @Test
    void inactivateAccountOfAgreement() {
    }

    @Test
    void changeStatusOfAgreement() {
    }

    @Test
    void changeInterestRateForAgreement() {
    }

    @Test
    void getCurrentAccountId() {
    }

    @Test
    void getAccess() {
    }
}