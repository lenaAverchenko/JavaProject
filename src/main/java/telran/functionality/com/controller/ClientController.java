package telran.functionality.com.controller;
/**
 * Class ClientController - Rest controller, to give answer to user's request. It contains methods to work
 * with client information.
 *
 * @author Olena Averchenko
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;

import telran.functionality.com.entity.Client;

import telran.functionality.com.entity.ClientData;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.ForbiddenLoginNameException;
import telran.functionality.com.repository.ClientDataRepository;
import telran.functionality.com.service.ClientDataService;
import telran.functionality.com.service.ClientService;


import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client Controller", description = "Rest controller, which contains methods to work with client information.")
public class ClientController {

    private final PasswordEncoder encoder;

    private final ClientDataService clientDataService;

    private final ClientDataRepository clientDataRepository;

    private final ClientService clientService;

    private final Converter<Client, ClientDto, ClientCreateDto> clientConverter;

    @Operation(
            summary = "Getting clients",
            description = "It allows us to get the list of all existing in database clients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(clientConverter::toDto).collect(Collectors.toList());
    }

    @Operation(
            summary = "Getting the client",
            description = "It allows us to get a certain client by its id, if it exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        return clientConverter.toDto(clientService.getById(id));
    }

    @Operation(
            summary = "Saving the client",
            description = "It allows us to save a new client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "406", description = "Login already exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping
    public ClientDto save(@RequestBody @Parameter(description = "New client to save") ClientCreateDto clientCreateDto) {
        Client client = clientService.save(clientConverter.toEntity(clientCreateDto));
            clientDataService.create(new ClientData(clientCreateDto.getLogin(),
                    encoder.encode(clientCreateDto.getPassword()), client));
        return clientConverter.toDto(client);
    }

    @Operation(
            summary = "Update information",
            description = "It allows us to update personal information about the client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @Transactional
    @PutMapping("/updateInformation/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                                       @RequestBody @Parameter(description = "New data for the client to update information") ClientCreateDto clientCreateDto) {
        Client client = null;
        try{
            ClientData foundClientData = clientDataRepository.findAll()
                    .stream().filter(cl -> cl.getClient().getId().equals(id))
                    .findFirst().orElse(null);
            if(foundClientData != null){
                clientCreateDto.setLogin(foundClientData.getLogin());
            }
            client = clientService.updatePersonalInfo(id, clientConverter.toEntity(clientCreateDto));
        } catch (ForbiddenLoginNameException ex){
            //
        }
        return clientConverter.toDto(client);
    }

      @Operation(
            summary = "Deleting the client",
            description = "It allows us to delete the client by its id, if it exists, and if the balances of its accounts are empty",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data."),
                    @ApiResponse(responseCode = "403", description = "Unable to delete bank client."),
                    @ApiResponse(responseCode = "403", description = "Not empty balance.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        clientService.delete(id);
    }


    @Operation(
            summary = "Change manager",
            description = "It allows us to change a manager for the client, if it's acceptable",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeManagerForClient/{id}/{managerId}")
    public void changeManager(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                              @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId) {
        clientService.changeManager(id, managerId);
    }


    @Operation(
            summary = "Change status of the client",
            description = "It allows us to change status of the client.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                             @PathVariable(name = "status") @Parameter(description = "New status for the client") Status status) {
        clientService.changeStatus(id, status);
    }

    @Operation(
            summary = "Inactivate the client",
            description = "It allows us to change status of the client to 'INACTIVE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "500", description = "Internal error"),
                    @ApiResponse(responseCode = "401", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        clientService.inactivateStatus(id);
    }
}
