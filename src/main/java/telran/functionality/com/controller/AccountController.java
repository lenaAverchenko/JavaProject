package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.service.AccountServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable(name = "id") UUID id) {
        return accountService.getById(id);
    }

    @PostMapping
    public Account save(@RequestBody Account account){
        return accountService.create(account);
    }
}
