package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.ForbiddenAccessException;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.AgreementService;
import telran.functionality.com.service.ClientDataService;


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
    private final ClientDataService clientDataService;
    @Autowired
    private PasswordEncoder encoder;


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

    @GetMapping("/client/show")
    public AccountDto getById(@RequestParam UUID id) {
        getAccess(id);
        return accountConverter.toDto(accountService.getById(id));
    }

    @PostMapping("/client")
    public AccountDto save(@RequestBody AccountCreateDto accountCreateDto) {
        return accountConverter.toDto(accountService.save(accountConverter.toEntity(accountCreateDto)));
    }

    @PutMapping("/updateStatus")
    public AccountDto updateStatus(@RequestParam UUID id, @RequestParam Status newStatus) {
        return accountConverter.toDto(accountService.changeStatus(id, newStatus));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam UUID iban) {
        accountService.delete(iban);
    }

    @GetMapping("/client/getBalanceOf")
    public BalanceDto getBalanceOf(@RequestParam UUID id) {
        getAccess(id);
        return accountService.getBalanceOf(id);
    }

    @GetMapping("/client/getHistoryOf")
    public List<TransactionDto> getHistoryOfTransactionsByAccountId(@RequestParam UUID id) {
        getAccess(id);
        return accountService.getHistoryOfTransactionsByAccountId(id).stream()
                .map(transactionConverter::toDto).collect(Collectors.toList());
    }

    @PutMapping("/client/transferMoneyBetweenAccounts")
    public void transferMoneyBetweenAccounts(@RequestBody TransactionCreateDto transactionCreateDto) {
        getAccess(transactionCreateDto.getDebitAccountId());
        accountService.transferMoneyBetweenAccounts(transactionConverter.toEntity(transactionCreateDto));
    }

    @PutMapping("/client/withdrawMoneyFrom")
    public AccountDto withdrawMoney(@RequestParam UUID clientId,
                                    @RequestParam UUID accountId,
                                    @RequestParam double sum) {
        getAccess(accountId);
        return accountConverter.toDto(accountService.withdrawMoney(clientId, accountId, sum));
    }

    @PutMapping("/client/putMoneyTo")
    public AccountDto putMoney(@RequestParam UUID accountId,
                               @RequestParam double sum) {
        return accountConverter.toDto(accountService.putMoney(accountId, sum));
    }

    @PostMapping("/agreements/createNewDeal")
    public void createNewDeal(@RequestBody AgreementCreateDto agreement) {
        agreementService.save(agreementConverter.toEntity(agreement));
        accountService.changeStatus(agreement.getAccountId(), Status.ACTIVE);
    }

    @PutMapping("/inactivateAccount")
    public void inactivateAccount(@RequestParam UUID id) {
        accountService.inactivateAccount(id);
    }

    @GetMapping("/accountBelongsToClient")
    public boolean accountBelongsToClient(@RequestParam UUID clientId, @RequestParam UUID accountId) {
        return accountService.accountBelongsToClient(accountId, clientId);
    }

    @PutMapping("/agreements/inactivateAgreement")
    public void inactivateAccountOfAgreement(@RequestParam long id) {
        agreementService.inactivateAgreement(id);
    }

    @PutMapping("/agreements/changeStatus")
    public void changeStatusOfAgreement(@RequestParam long id, @RequestParam Status status) {
        agreementService.changeStatus(id, status);
    }

    @PutMapping("/agreements/changeInterestRate")
    public void changeInterestRateForAgreement(@RequestParam long id, @RequestParam double newRate) {
        agreementService.changeInterestRate(id, newRate);
    }

    public UUID getCurrentAccountId(){
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) currentAuthentication.getPrincipal();
        String currentClientLogin = currentUser.getUsername();
        ClientData clientData = clientDataService.getByLogin(currentClientLogin);
        if (clientData != null){
            return clientData.getClient().getId();
        }
        return null;
    }

    public void getAccess(UUID id) {
        UUID currentClientId = getCurrentAccountId();
        if (currentClientId != null) {
            if (!currentClientId.equals(accountService.getById(id).getClient().getId())) {
                throw new ForbiddenAccessException("Access to the account " + id + " with the current access is denied.");
            }
        }
    }

}
