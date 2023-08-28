package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ClientCreateDto;
import telran.functionality.com.dto.ClientDto;

import telran.functionality.com.entity.Client;

import telran.functionality.com.enums.Status;
import telran.functionality.com.service.ClientService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {


    private final ClientService clientService;

    private final Converter<Client, ClientDto, ClientCreateDto> clientConverter;



    @GetMapping
    public List<ClientDto> getAll() {
        List<ClientDto> allClients = clientService.getAll().stream()
                .map(clientConverter::toDto).collect(Collectors.toList());
        return allClients;
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable(name = "id") UUID id) {
        return clientConverter.toDto(clientService.getById(id));
    }

    @PostMapping
    public ClientDto save(@RequestBody ClientCreateDto clientCreateDto) {
        return clientConverter.toDto(clientService.save(clientConverter.toEntity(clientCreateDto)));
    }

    @PutMapping("/updateInformation/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") UUID id, @RequestBody ClientCreateDto clientCreateDto) {
        return clientConverter.toDto(clientService.updatePersonalInfo(id, clientConverter.toEntity(clientCreateDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        clientService.delete(id);
    }

    @PutMapping("/changeManagerForClient/{id}/{managerId}")
    public void changeManager(@PathVariable(name = "id") UUID id, @PathVariable(name = "managerId") long managerId) {
        clientService.changeManager(id, managerId);
    }

    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "status") Status status) {
        clientService.changeStatus(id, status);
    }

    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") UUID id) {
        clientService.inactivateStatus(id);
    }

}
