package telran.functionality.com.controller;
/**
 * Class ClientController - Rest controller, to give answer to user's request. It contains methods to work
 * with client information.
 *
 * @author Olena Averchenko
 */
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ClientCreateDto;
import telran.functionality.com.dto.ClientDto;
import org.springframework.security.core.userdetails.User;

import telran.functionality.com.entity.Client;

import telran.functionality.com.entity.ClientData;
//import telran.functionality.com.entity.Role;
import telran.functionality.com.enums.Status;
import telran.functionality.com.service.ClientDataService;
import telran.functionality.com.service.ClientService;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final PasswordEncoder encoder;

    private final ClientDataService clientDataService;

    private final ClientService clientService;

    private final Converter<Client, ClientDto, ClientCreateDto> clientConverter;

    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(clientConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/showClient")
    public ClientDto getById(@RequestParam UUID id) {
        return clientConverter.toDto(clientService.getById(id));
    }

    @PostMapping
    public ClientDto save(@RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientService.save(clientConverter.toEntity(clientCreateDto));
        clientDataService.create(new ClientData(clientCreateDto.getLogin(),
                encoder.encode(clientCreateDto.getPassword()), client));
        return clientConverter.toDto(client);
    }

    @PutMapping("/updateInformation/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") UUID id, @RequestBody ClientCreateDto clientCreateDto) {
        return clientConverter.toDto(clientService.updatePersonalInfo(id, clientConverter.toEntity(clientCreateDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        clientService.delete(id);
    }

    @PutMapping("/changeManagerForClient")
    public void changeManager(@RequestParam UUID id, @RequestParam long managerId) {
        clientService.changeManager(id, managerId);
    }

    @PutMapping("/changeStatus")
    public void changeStatus(@RequestParam UUID id, @RequestParam Status status) {
        clientService.changeStatus(id, status);
    }

    @PutMapping("/inactivateStatus")
    public void inactivateStatus(@RequestParam UUID id) {
        clientService.inactivateStatus(id);
    }
}
