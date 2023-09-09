package telran.functionality.com.controller;
/**
 * Class ClientController - Rest controller, to give answer to user's request. It contains methods to work
 * with client information.
 *
 * @author Olena Averchenko
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import telran.functionality.com.repository.ClientDataRepository;
import telran.functionality.com.service.ClientDataService;
import telran.functionality.com.service.ClientService;


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

    //    @ApiOperation(value = "Getting all the clients", response = ClientDto.class, responseContainer = "List")
//    @ApiResponse(code = 404, message = "There is no one registered client")
    @Operation(
            summary = "Getting clients",
            description = "It allows us to get the list of all existing in database clients"
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(clientConverter::toDto).collect(Collectors.toList());
    }

    //    @ApiOperation(value = "Getting the client by its id", response = ClientDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParam(name = "id", value = "Identificator for the client", required = true, dataTypeClass = UUID.class, paramType = "path")
    @Operation(
            summary = "Getting the client",
            description = "It allows us to get a certain client by its id, if it exists"
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        return clientConverter.toDto(clientService.getById(id));
    }

    //    @ApiOperation(value = "Save the client", response = ClientDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 406, message = "Client with login ... already exists.")
//    })
//    @ApiImplicitParam(name = "clientCreateDto", value = "New client to save", required = true, dataTypeClass = ClientCreateDto.class, paramType = "body")
    @Operation(
            summary = "Saving the client",
            description = "It allows us to save a new client"
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping
    public ClientDto save(@RequestBody @Parameter(description = "New client to save") ClientCreateDto clientCreateDto) {
        Client client = clientService.save(clientConverter.toEntity(clientCreateDto));
        if (!clientDataRepository.findAll()
                .stream().map(ClientData::getLogin).toList()
                .contains(clientCreateDto.getLogin())){
            clientDataService.create(new ClientData(clientCreateDto.getLogin(),
                    encoder.encode(clientCreateDto.getPassword()), client));
        }
        return clientConverter.toDto(client);
    }

    //    @ApiOperation(value = "Update the client", response = ClientDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the existing client", required = true, dataTypeClass = UUID.class, paramType = "path"),
//            @ApiImplicitParam(name = "clientCreateDto", value = "New information about the client", required = true, dataTypeClass = ClientCreateDto.class, paramType = "body")
//    })
    @Operation(
            summary = "Update information",
            description = "It allows us to update personal information about the client"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/updateInformation/{id}")
    public ClientDto updateInformation(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                                       @RequestBody @Parameter(description = "New data for the client to update information") ClientCreateDto clientCreateDto) {
        return clientConverter.toDto(clientService.updatePersonalInfo(id, clientConverter.toEntity(clientCreateDto)));
    }

    //    @ApiOperation(value = "Deleting client by its id")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 403, message = "Unable to delete bank client. It belongs to the bank."),
//            @ApiResponse(code = 403, message = "Please, withdraw money from your account before deleting.")
//    })
//    @ApiImplicitParam(name = "id", value = "Identificator for the client", required = true, dataTypeClass = UUID.class, paramType = "path")
    @Operation(
            summary = "Deleting the client",
            description = "It allows us to delete the client by its id, if it exists, and if the balances of its accounts are empty"
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        clientService.delete(id);
    }

    //    @ApiOperation(value = "Change manager for the client")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the client", required = true, dataTypeClass = UUID.class, paramType = "path"),
//            @ApiImplicitParam(name = "managerId", value = "Id of the manager", required = true, dataTypeClass = Long.class, paramType = "path")
//    })
    @Operation(
            summary = "Change manager",
            description = "It allows us to change a manager for the client, if it's acceptable"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeManagerForClient/{id}/{managerId}")
    public void changeManager(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                              @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId) {
        clientService.changeManager(id, managerId);
    }

    //    @ApiOperation(value = "Update status of the client")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the existing client", required = true, dataTypeClass = UUID.class, paramType = "path"),
//            @ApiImplicitParam(name = "status", value = "New status for the client", required = true, dataTypeClass = Status.class, paramType = "path")
//    })
    @Operation(
            summary = "Change status of the client",
            description = "It allows us to change status of the client."
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id,
                             @PathVariable(name = "status") @Parameter(description = "New status for the client") Status status) {
        clientService.changeStatus(id, status);
    }

    //    @ApiOperation(value = "Inactivate status of the client")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Client with id ... doesn't exist")
//    })
//    @ApiImplicitParam(name = "id", value = "Id of the existing client", required = true, dataTypeClass = UUID.class, paramType = "path")
    @Operation(
            summary = "Inactivate the client",
            description = "It allows us to change status of the client to 'INACTIVE'."
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the client") UUID id) {
        clientService.inactivateStatus(id);
    }
}
