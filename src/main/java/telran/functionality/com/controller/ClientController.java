package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ClientCreateDto;
import telran.functionality.com.dto.ClientDto;

import telran.functionality.com.entity.Client;

import telran.functionality.com.service.ClientService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final Converter<Client, ClientDto, ClientCreateDto> clientConverter;

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);


    @GetMapping
    public List<ClientDto> getAll() {
        logger.info("Call method getAll clients");
        List<ClientDto> allClients = clientService.getAll().stream()
                .map(client -> clientConverter.toDto(client)).collect(Collectors.toList());
        logger.info("Method getAll clients has resulted with: " + allClients.toString());
        return allClients;
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable(name = "id") UUID id) {
        logger.info("Call method getById {}", id);
        ClientDto foundClient = clientConverter.toDto(clientService.getById(id));
        logger.info("Method getById {} result is: {}", id, foundClient);
        return foundClient;
    }

    @PostMapping
    public ClientDto save(@RequestBody ClientCreateDto clientCreateDto) {
        logger.info("Call method save client {}", clientCreateDto);
        ClientDto savedClient = clientConverter.toDto(clientService.create(clientConverter.toEntity(clientCreateDto)));
        logger.info("Method save client has resulted with: {}", savedClient);
        return savedClient;
    }

    @PutMapping("/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") UUID id, @RequestBody ClientCreateDto clientCreateDto) {
        logger.info("Call method updateInformation about client by id {} with information: {}", id, clientCreateDto);
        ClientDto updatedClientDto = clientConverter.toDto(clientService.updatePersonalInfo(id, clientConverter.toEntity(clientCreateDto)));
        logger.info("Method updateInformation has resulted with: {}", updatedClientDto);
        return updatedClientDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        logger.info("Call method delete client by id {}", id);
        clientService.delete(id);
        logger.info("Method delete client has ended");

    }

    @PutMapping("/changeManager/{id}/{managerId}")
    public void changeManager(@PathVariable(name = "id") UUID id, @PathVariable(name = "managerId") long managerId) {
        logger.info("Call method changeManager for client by id {} with new manaderId: {}", id, managerId);
        clientService.changeManager(id, managerId);
        logger.info("Method changeManager has ended");
    }

    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "status") int status) {
        logger.info("Call method changeStatus of client by id {} with the value: {}", id, status);
        clientService.changeStatus(id, status);
        logger.info("Method changeStatus has ended");
    }

    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") UUID id) {
        logger.info("Call method inactivateStatus for client with id {} ", id);
        clientService.inactivateStatus(id);
        logger.info("Method changeStatus has ended");
    }

}
