package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.ClientConverter;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.service.ClientService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientConverter clientConverter;

    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(client -> clientConverter.toDto(client)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable(name = "id") UUID id) {
        return clientConverter.toDto(clientService.getById(id));
    }

    @PostMapping
    public ClientDto save(@RequestBody ClientDto clientDto) {
        return clientConverter.toDto(clientService.create(clientConverter.toEntity(clientDto)));
    }

    @PutMapping("/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") UUID id, @RequestBody ClientDto clientDto){
        return clientConverter.toDto(clientService.update(id, clientConverter.toEntity(clientDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        clientService.delete(id);
    }
}
