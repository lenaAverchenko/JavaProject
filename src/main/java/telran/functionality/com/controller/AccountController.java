package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.AgreementService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountService accountService;
    @Autowired
    private final AgreementService agreementService;
    @Autowired
    private final Converter<Account, AccountDto, AccountCreateDto> accountConverter;
    @Autowired
    private final Converter<Agreement, AgreementDto, AgreementCreateDto> agreementConverter;
    @Autowired
    private final Converter<Transaction, TransactionDto, TransactionCreateDto> transactionConverter;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @GetMapping
    public List<AccountDto> getAll() {
        logger.info("Call method getAll accounts");
        List<AccountDto> allAccounts = accountService.getAll().stream()
                .map(account -> accountConverter.toDto(account))
                .collect(Collectors.toList());
        logger.info("Method getAll accounts has resulted with: " + allAccounts.toString());
        return allAccounts;
    }

    @GetMapping("/allAgreements")
    public List<AgreementDto> getAllAgreements() {
        logger.info("Call method getAll agreements from accounts");
        List<AgreementDto> allAgreements = agreementService.getAll().stream()
                .map(agreementConverter::toDto)
                .toList();
        logger.info("Method getAll agreements has resulted with: " + allAgreements);
        return allAgreements;
    }

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable(name = "id") UUID id) {
        logger.info("Call method getById {}", id);
        AccountDto foundAccount = accountConverter.toDto(accountService.getById(id));
        logger.info("Method getById {} result is: {}", id, foundAccount);
        return foundAccount;
    }

    @PostMapping
    public AccountDto save(@RequestBody AccountCreateDto accountCreateDto) {
        logger.info("Call method save account {}", accountCreateDto);
        AccountDto savedAccount = accountConverter.toDto(accountService.create(accountConverter.toEntity(accountCreateDto)));
        logger.info("Method save account has resulted with: {}", savedAccount);
        return savedAccount;
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public AccountDto updateStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "status") int newStatus) {
        logger.info("Call method updateStatus of account by id {} with information: {}", id, newStatus);
        AccountDto updatedAccount = accountConverter.toDto(accountService.changeStatus(id, newStatus));
        logger.info("Method updateInformation has resulted with: {}", updatedAccount);
        return updatedAccount;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        logger.info("Call method delete account by id {}", id);
        accountService.delete(id);
        logger.info("Method delete client has ended");
    }

    @GetMapping("/getBalanceOf/{id}")
    public double getBalanceOf(@PathVariable(name = "id") UUID id) {
        logger.info("Call method getBalanceOf accountId: {}", id);
        double currentBalance = accountService.getBalanceOf(id);
        logger.info("Method getBalanceOf has ended with result: {}", currentBalance);
        return currentBalance;
    }

    @GetMapping("/getHistoryOf/{id}")
    public List<TransactionDto> getHistoryOfTransactionsByAccountId(@PathVariable(name = "id") UUID id) {
        logger.info("Call method getHistoryOfTransactionsByAccountId: {}", id);
        List<TransactionDto> listOfTransactions = accountService.getHistoryOfTransactionsByAccountId(id).stream()
                .map(transactionConverter::toDto).collect(Collectors.toList());
        logger.info("Method getHistoryOfTransactionsByAccountId has ended with result: {}", listOfTransactions);
        return listOfTransactions;
    }

    @PutMapping("/transferMoneyBetweenAccounts/{debitAccountId}/{creditAccountId}/{sum}/{type}/{description}")
    public void transferMoneyBetweenAccounts(@PathVariable(name = "debitAccountId") UUID debitAccountId,
                                             @PathVariable(name = "creditAccountId") UUID creditAccountId,
                                             @PathVariable(name = "sum") double sum,
                                             @PathVariable(name = "type") int type,
                                             @PathVariable(name = "description") String description) {
        logger.info("Call method transferMoneyBetweenAccounts to transfer {} from {} to {}", sum, debitAccountId, creditAccountId);
        accountService.transferMoneyBetweenAccounts(debitAccountId, creditAccountId, sum, type, description);
        logger.info("Method transferMoneyBetweenAccounts has ended");
    }

    @PutMapping("/withdrawMoneyFrom/{clientId}/{accountId}/{sum}")
    public AccountDto withdrawMoney(@PathVariable(name = "clientId") UUID clientId,
                                    @PathVariable(name = "accountId") UUID accountId,
                                    @PathVariable(name = "sum") double sum) {
        logger.info("Call method withdrawMoney {} from {}", sum, accountId);
        AccountDto updatedAccount = accountConverter.toDto(accountService.withdrawMoney(clientId, accountId, sum));
        logger.info("Method withdrawMoney has ended and resulted with: {}", updatedAccount);
        return updatedAccount;
    }

    @PutMapping("/putMoneyTo/{accountId}/{sum}")
    public AccountDto putMoney(@PathVariable(name = "accountId") UUID accountId,
                               @PathVariable(name = "sum") double sum) {
        logger.info("Call method putMoney {} to {}", sum, accountId);
        AccountDto updatedAccount = accountConverter.toDto(accountService.putMoney(accountId, sum));
        logger.info("Method putMoney has ended and resulted with: {}", updatedAccount);
        return updatedAccount;
    }

    @PostMapping("/agreements/createNewDeal")
    public void createNewDeal(@RequestBody AgreementCreateDto agreement) {
        logger.info("Call method createNewDeal and make agreement {}", agreement);
        agreementService.create(agreementConverter.toEntity(agreement));
//        AgreementDto createdAgreement = agreementConverter.toDto(agreementService.create(agreement));
        Account accountWithValidStatus = accountService.changeStatus(agreement.getAccountId(), 1);
        logger.info("Method createNewDeal has ended and changed status for its account: {}", accountWithValidStatus);
//        return createdAgreement;
    }

    @PutMapping("/inactivateAccount/{accountId}")
    public void inactivateAccount(@PathVariable(name = "accountId") UUID id) {
        logger.info("Call method inactivateAccount by id {}", id);
        accountService.inactivateAccount(id);
        logger.info("Method inactivateAccount has ended");
    }

    @GetMapping("/accountBelongsToClient/{accountId}/{clientId}")
    public boolean accountBelongsToClient(@PathVariable(name = "accountId") UUID accountId, @PathVariable(name = "clientId") UUID clientId) {
        logger.info("Call method accountBelongsToClient for accountId {} and clientId {}", accountId, clientId);
        boolean result = accountService.accountBelongsToClient(accountId, clientId);
        logger.info("Method accountBelongsToClient has ended");
        return result;
    }

    @PutMapping("/agreements/inactivateAgreement/{id}")
    public void inactivateAccountOfAgreement(@PathVariable(name = "id") long id) {
        logger.info("Call method inactivateAccountOfAgreement by id {}", id);
        agreementService.inactivateAgreement(id);
        logger.info("Method inactivateAccountOfAgreement has ended");
    }

    @PutMapping("/agreements/changeStatus/{id}/{status}")
    public void changeStatusOfAgreement(@PathVariable(name = "id") long id, @PathVariable(name = "status") int status) {
        logger.info("Call method changeStatusOfAgreement by id {} to {}", id, status);
        agreementService.changeStatus(id, status);
        logger.info("Method changeStatusOfAgreement has ended");
    }

    @PutMapping("/agreements/changeInterestRate/{id}/{newRate}")
    public void changeInterestRateForAgreement(@PathVariable(name = "id") long id, @PathVariable(name = "newRate") double newRate) {
        logger.info("Call method changeInterestRateForAgreement by id {} to {}", id, newRate);
        agreementService.changeInterestRate(id, newRate);
        logger.info("Method changeInterestRateForAgreement has ended");
    }
}
