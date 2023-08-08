package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.service.AgreementService;

import java.util.List;


@RestController
@RequestMapping("/agreement")
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @GetMapping
    public List<Agreement> getAll() {
        return agreementService.getAll();
    }

    @GetMapping("/{id}")
    public Agreement getById(@PathVariable(name = "id") long id) {
        return agreementService.getById(id);
    }

    @PostMapping
    public Agreement save(@RequestBody Agreement agreement) {
        return agreementService.create(agreement);
    }
}
