package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.AgreementService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final AgreementService agreementService;
    private final Converter<Account, AccountDto, AccountCreateDto> accountConverter;
    private final Converter<Agreement, AgreementDto, AgreementCreateDto> agreementConverter;
    private final Converter<Transaction, TransactionDto, TransactionCreateDto> transactionConverter;



    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll().stream()
                .map(accountConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/allAgreements")
    public List<AgreementDto> getAllAgreements() {
        return agreementService.getAll().stream()
                .map(agreementConverter::toDto)
                .toList();
    }

    @GetMapping("/{iban}")
    public AccountDto getById(@PathVariable(name = "iban") UUID iban) {
        return accountConverter.toDto(accountService.getByIban(iban));
    }

    @PostMapping
    public AccountDto save(@RequestBody AccountCreateDto accountCreateDto) {
        return accountConverter.toDto(accountService.save(accountConverter.toEntity(accountCreateDto)));
    }

    @PutMapping("/updateStatus/{iban}/{status}")
    public AccountDto updateStatus(@PathVariable(name = "iban") UUID iban, @PathVariable(name = "status") Status newStatus) {
        return accountConverter.toDto(accountService.changeStatus(iban, newStatus));
    }

    @DeleteMapping("/delete/{iban}")
    public void delete(@PathVariable(name = "iban") UUID iban) {
        accountService.delete(iban);
    }

    @GetMapping("/getBalanceOf/{iban}")
    public BalanceDto getBalanceOf(@PathVariable(name = "iban") UUID iban) {
        return accountService.getBalanceOf(iban);
    }

    @GetMapping("/getHistoryOf/{iban}")
    public List<TransactionDto> getHistoryOfTransactionsByAccountId(@PathVariable(name = "iban") UUID iban) {
        return accountService.getHistoryOfTransactionsByAccountIban(iban).stream()
                .map(transactionConverter::toDto).collect(Collectors.toList());
    }

    @PutMapping("/transferMoneyBetweenAccounts/{debitAccountIban}/{creditAccountIban}/{sum}/{type}/{description}")
    public void transferMoneyBetweenAccounts(@PathVariable(name = "debitAccountIban") UUID debitAccountIban,
                                             @PathVariable(name = "creditAccountIban") UUID creditAccountIban,
                                             @PathVariable(name = "sum") double sum,
                                             @PathVariable(name = "type") Type type,
                                             @PathVariable(name = "description") String description) {
        accountService.transferMoneyBetweenAccounts(debitAccountIban, creditAccountIban, sum, type, description);
    }

    @PutMapping("/withdrawMoneyFrom/{clientId}/{accountIban}/{sum}")
    public AccountDto withdrawMoney(@PathVariable(name = "clientId") UUID clientId,
                                    @PathVariable(name = "accountIban") UUID accountIban,
                                    @PathVariable(name = "sum") double sum) {
        return accountConverter.toDto(accountService.withdrawMoney(clientId, accountIban, sum));
    }

    @PutMapping("/putMoneyTo/{accountIban}/{sum}")
    public AccountDto putMoney(@PathVariable(name = "accountIban") UUID accountIban,
                               @PathVariable(name = "sum") double sum) {
        return accountConverter.toDto(accountService.putMoney(accountIban, sum));
    }

    @PostMapping("/agreements/createNewDeal")
    public void createNewDeal(@RequestBody AgreementCreateDto agreement) {
        agreementService.save(agreementConverter.toEntity(agreement));
        accountService.changeStatus(agreement.getAccountId(), Status.ACTIVE);
    }

    @PutMapping("/inactivateAccount/{accountIban}")
    public void inactivateAccount(@PathVariable(name = "accountIban") UUID iban) {
        accountService.inactivateAccount(iban);
    }

    @GetMapping("/accountBelongsToClient/{clientId}/{accountIban}")
    public boolean accountBelongsToClient(@PathVariable(name = "clientId") UUID clientId, @PathVariable(name = "accountIban") UUID accountIban) {
        return accountService.accountBelongsToClient(accountIban, clientId);
    }

    @PutMapping("/agreements/inactivateAgreement/{id}")
    public void inactivateAccountOfAgreement(@PathVariable(name = "id") long id) {
        agreementService.inactivateAgreement(id);
    }

    @PutMapping("/agreements/changeStatus/{id}/{status}")
    public void changeStatusOfAgreement(@PathVariable(name = "id") long id, @PathVariable(name = "status") Status status) {
        agreementService.changeStatus(id, status);
    }

    @PutMapping("/agreements/changeInterestRate/{id}/{newRate}")
    public void changeInterestRateForAgreement(@PathVariable(name = "id") long id, @PathVariable(name = "newRate") double newRate) {
        agreementService.changeInterestRate(id, newRate);
    }

}
