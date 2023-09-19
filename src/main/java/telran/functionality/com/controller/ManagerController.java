package telran.functionality.com.controller;
/**
 * Class ManagerController - Rest controller, to give answer to user's request. It contains methods to work
 * with manager information, including managing information about projects
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
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Status;
import telran.functionality.com.service.ManagerDataService;
import telran.functionality.com.service.ManagerService;

import javax.transaction.Transactional;
import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@Tag(name = "Manager Controller", description = "Rest controller, which contains methods to work with manager information, including managing information about projects")
public class ManagerController {

    private final PasswordEncoder encoder;
    private final ManagerService managerService;
    private final Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    private final Converter<Product, ProductDto, ProductCreateDto> productConverter;
    private final ManagerDataService managerDataService;

    @Operation(
            summary = "Getting managers",
            description = "It allows us to get the list of all existing in database managers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Empty lst of managers")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerConverter::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Getting the manager",
            description = "It allows us to get a certain manager by its id, if it exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        return managerConverter.toDto(managerService.getById(id));
    }

    @Operation(
            summary = "Saving the manager",
            description = "It allows us to save a new manager",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "406", description = "Login is already exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @Transactional
    @PostMapping
    public ManagerDto save(@RequestBody @Parameter(description = "New manager to save") ManagerCreateDto managerCreateDto) {
        Manager manager = managerService.save(managerConverter.toEntity(managerCreateDto));
        managerDataService.create(new ManagerData(
                managerCreateDto.getLogin(),
                encoder.encode(managerCreateDto.getPassword()),
                manager));
        return managerConverter.toDto(manager);
    }


    @Operation(
            summary = "Update information",
            description = "It allows us to update personal information about the manager",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @Transactional
    @PutMapping("/updateInformation/{id}")
    public ManagerDto updateInformation(
            @PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id,
            @RequestBody @Parameter(description = "New information for manager to update") ManagerCreateDto managerCreateDto) {
        Manager manager = managerService.update(id, managerConverter.toEntity(managerCreateDto));
        return managerConverter.toDto(manager);
    }

    @Operation(
            summary = "Deleting the manager",
            description = "It allows us to delete the manager by its id, if it exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        managerService.delete(id);
    }

    @Operation(
            summary = "Change status",
            description = "It allows us to change status of the manager.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id,
                             @PathVariable(name = "status") @Parameter(description = "New status for the manager") Status status) {
        managerService.changeStatus(id, status);
    }

    @Operation(
            summary = "Inactivate the manager",
            description = "It allows us to change status of the manager to 'INACTIVE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        managerService.inactivateStatus(id);
    }

    @Operation(
            summary = "Adding the product",
            description = "It allows us to add a new product to the manager",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data."),
                    @ApiResponse(responseCode = "409", description = "Conflicted data - not the same"),
                    @ApiResponse(responseCode = "451", description = "Manager is not active.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/addProductTo/{id}")
    public ManagerDto addProduct(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id,
                                 @RequestBody @Parameter(description = "New product to add") ProductCreateDto productDto) {
        return managerConverter.toDto(managerService.addProduct(id, productConverter.toEntity(productDto)));
    }

    @Operation(
            summary = "Change status of the product",
            description = "It allows us to change status of the product, if it belongs to the manager.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "404", description = "Product doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data."),
                    @ApiResponse(responseCode = "409", description = "Product doesn't belong to manager")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatusOfProductForManager/{managerId}/{productId}/{status}")
    public void changeStatusOfProduct(
            @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId,
            @PathVariable(name = "productId") @Parameter(description = "Identifier of the product") long productId,
            @PathVariable(name = "status") @Parameter(description = "New status for the product") Status status) {
        managerService.changeStatusOfProduct(managerId, productId, status);
    }

    @Operation(
            summary = "Change manager",
            description = "It allows us to change a manager for the product, if it's acceptable",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Manager doesn't exist"),
                    @ApiResponse(responseCode = "404", description = "Product doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeManagerOfProduct/{id}/{managerId}")
    public void changeManagerOfProduct(
            @PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
            @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId) {
        managerService.changeManagerOfProduct(id, managerId);
    }

    @Operation(
            summary = "Change status of the product",
            description = "It allows us to change status of the product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Product doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatusOfProduct/{id}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
                                      @PathVariable(name = "status") @Parameter(description = "New status for the product") Status status) {
        managerService.changeStatusOfProduct(id, status);
    }

    @Operation(
            summary = "Change limit",
            description = "It allows us to change the limit for the product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Product doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeLimitValueOfProduct/{id}/{limitValue}")
    public void changeLimitValueOfProduct(
            @PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
            @PathVariable(name = "limitValue") @Parameter(description = "New value of the limit") int limitValue) {
        managerService.changeLimitValueOfProduct(id, limitValue);
    }

    @Operation(
            summary = "Inactivate the product",
            description = "It allows us to change status of the product to 'INACTIVE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Product doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @PutMapping("/inactivateStatusOfProduct/{productId}")
    public void inactivateStatusOfProduct(@PathVariable(name = "productId") @Parameter(description = "Identifier of the product") long productId) {
        managerService.inactivateStatusOfProduct(productId);
    }
}
