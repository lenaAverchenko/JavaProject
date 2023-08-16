package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.AccountDto;
import telran.functionality.com.dto.TransactionDto;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.service.AccountService;


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
    private final Converter<Account, AccountDto> accountConverter;
    @Autowired
    private final Converter<Transaction, TransactionDto> transactionConverter;

    @GetMapping
    public List<AccountDto> getAll() {

        return accountService.getAll().stream()
                .map(account -> accountConverter.toDto(account))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable(name = "id") UUID id) {
        return accountConverter.toDto(accountService.getById(id));
    }

    @PostMapping
    public AccountDto save(@RequestBody AccountDto accountDto) {
        return accountConverter.toDto(accountService.create(accountConverter.toEntity(accountDto)));
    }

    @PutMapping("/{id}")
    public AccountDto update(@PathVariable(name = "id") UUID id, @RequestBody AccountDto accountDto) {
        return accountConverter.toDto(accountService.update(id, accountConverter.toEntity(accountDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        accountService.delete(id);
    }

    @GetMapping("/getBalance/{id}")
    public double getBalanceOf(@PathVariable(name = "id") UUID id) {
        return accountService.getBalanceOf(id);
    }

    @GetMapping("/getHistory/{id}")
    public List<TransactionDto> getHistoryOfTransactionsByAccountId(@PathVariable(name = "id") UUID id) {
        return accountService.getHistoryOfTransactionsByAccountId(id).stream()
                .map(transaction -> transactionConverter.toDto(transaction)).collect(Collectors.toList());
    }

    @PutMapping("/transferMoneyBetweenAccounts/{debitAccountId}/{creditAccountId}/{sum}/{type}/{description}")
    public void transferMoneyBetweenAccounts(@PathVariable(name = "debitAccountId") UUID debitAccountId,
                                             @PathVariable(name = "creditAccountId") UUID creditAccountId,
                                             @PathVariable(name = "sum") double sum,
                                             @PathVariable(name = "type") int type,
                                             @PathVariable(name = "description") String description) {
        accountService.transferMoneyBetweenAccounts(debitAccountId, creditAccountId, sum, type, description);
    }
}
