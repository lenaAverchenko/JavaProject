package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.entity.Client;
import telran.functionality.com.service.ClientService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable(name = "id") UUID id) {
        return clientService.getById(id);
    }

    @PostMapping
    public Client save(@RequestBody Client client) {
        return clientService.create(client);
    }

}
