package telran.functionality.com.controller;
/**
 * Class ManagerController - Rest controller, to give answer to user's request. It contains methods to work
 * with manager information, including managing information about projects
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
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;
import telran.functionality.com.repository.ManagerDataRepository;
import telran.functionality.com.service.ManagerDataService;
import telran.functionality.com.service.ManagerService;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@Tag(name = "Manager Controller", description = "Rest controller, which contains methods to work with manager information, including managing information about projects")
public class ManagerController {

    private final PasswordEncoder encoder;
    private final ManagerService managerService;

    private final ManagerDataRepository managerDataRepository;
    private final Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    private final Converter<Product, ProductDto, ProductCreateDto> productConverter;
    private final ManagerDataService managerDataService;

//    @Operation(summary = "Getting the list of the existing managers")
//    @ApiResponses({
//            @ApiResponse(responseCode = "401", description = "Access denied"),
//            @ApiResponse(responseCode = "404", description = "There is no one registered manager")
//    })
//    @SecurityRequirement(name = "BasicAuth")
    @Operation(
            summary = "Getting managers",
            description = "It allows us to get the list of all existing in database managers"
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerConverter::toDto)
                .collect(Collectors.toList());
    }

//    @ApiOperation(value = "Getting the manager by its id", response = ManagerDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParam(name = "id", value = "Identificator for the manager", required = true, dataTypeClass = Long.class, paramType = "path")
    @Operation(
            summary = "Getting the manager",
            description = "It allows us to get a certain manager by its id, if it exists"
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        return managerConverter.toDto(managerService.getById(id));
    }

//    @ApiOperation(value = "Save the manager", response = ManagerDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 406, message = "Manager with login ... already exists.")
//    })
//    @ApiImplicitParam(name = "managerCreateDto", value = "New manager to save", required = true, dataTypeClass = ManagerCreateDto.class, paramType = "body")
    @Operation(
            summary = "Saving the manager",
            description = "It allows us to save a new manager"
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping
    public ManagerDto save(@RequestBody @Parameter(description = "New manager to save") ManagerCreateDto managerCreateDto) {
        Manager manager = managerService.save(managerConverter.toEntity(managerCreateDto));
        if (!managerDataRepository.findAll()
                .stream().map(ManagerData::getLogin).toList()
                .contains(managerCreateDto.getLogin())){
            managerDataService.create(new ManagerData(
                    managerCreateDto.getLogin(),
                    encoder.encode(managerCreateDto.getPassword()),
                    manager));
        }
        return managerConverter.toDto(manager);
    }

//    @ApiOperation(value = "Update the manager", response = ManagerDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist")    ,
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the existing manager", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "managerCreateDto", value = "New information about the manager", required = true, dataTypeClass = ManagerCreateDto.class, paramType = "body")
//    })
    @Operation(
            summary = "Update information",
            description = "It allows us to update personal information about the manager"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/updateInformation/{id}")
    public ManagerDto updateInformation(
            @PathVariable (name = "id") @Parameter(description = "Identifier of the manager") long id,
            @RequestBody @Parameter(description = "New information for manager to update") ManagerCreateDto managerCreateDto) {
        return managerConverter.toDto(managerService.update(id, managerConverter.toEntity(managerCreateDto)));
    }

//    @ApiOperation(value = "Deleting manager by its id")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParam(name = "id", value = "Identificator for the manager", required = true, dataTypeClass = Long.class, paramType = "path")
    @Operation(
            summary = "Deleting the manager",
            description = "It allows us to delete the manager by its id, if it exists"
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        managerService.delete(id);
    }

//    @ApiOperation(value = "Change status of the manager")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the existing manager", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "status", value = "New status for the manager", required = true, dataTypeClass = Status.class, paramType = "path")
//    })
    @Operation(
            summary = "Change status",
            description = "It allows us to change status of the manager."
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id,
                             @PathVariable(name = "status") @Parameter(description = "New status for the manager") Status status) {
        managerService.changeStatus(id, status);
    }

//    @ApiOperation(value = "Inactivate status of the manager")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist")
//    })
//    @ApiImplicitParam(name = "id", value = "Id of the existing manager", required = true, dataTypeClass = Long.class, paramType = "path")
    @Operation(
            summary = "Inactivate the manager",
            description = "It allows us to change status of the manager to 'INACTIVE'."
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id) {
        managerService.inactivateStatus(id);
    }

//    @ApiOperation(value = "Add a new product to the manager", response = ManagerDto.class)
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 409, message = "Provided id's are not the same. Check the data."),
//            @ApiResponse(code = 451, message = "Unable to get access to the manager with id %d.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the existing manager", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "productDto", value = "New product for the manager", required = true, dataTypeClass = ProductDto.class, paramType = "body")
//    })
    @Operation(
            summary = "Adding the product",
            description = "It allows us to add a new product to the manager"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/addProductTo/{id}")
    public ManagerDto addProduct(@PathVariable(name = "id") @Parameter(description = "Identifier of the manager") long id,
                                 @RequestBody @Parameter(description = "New product to add") ProductCreateDto productDto) {
        return managerConverter.toDto(managerService.addProduct(id, productConverter.toEntity(productDto)));
    }

//    @ApiOperation(value = "Change status of the product, if it belongs to the manager")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 404, message = "Product with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data."),
//            @ApiResponse(code = 409, message = "Product with id ... doesn't belong to manager with id ...")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "managerId", value = "Id of the manager", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "productId", value = "Id of the product", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "status", value = "New status for the product", required = true, dataTypeClass = Status.class, paramType = "path")
//    })
    @Operation(
            summary = "Change status of the product",
            description = "It allows us to change status of the product, if it belongs to the manager."
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatusOfProductForManager/{managerId}/{productId}/{status}")
    public void changeStatusOfProduct(
            @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId,
            @PathVariable(name = "productId") @Parameter(description = "Identifier of the product") long productId,
            @PathVariable(name = "status") @Parameter(description = "New status for the product") Status status) {
        managerService.changeStatusOfProduct(managerId, productId, status);
    }

//    @ApiOperation(value = "Change manager for the product")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Manager with id ... doesn't exist"),
//            @ApiResponse(code = 404, message = "Product with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the product", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "managerId", value = "Id of the manager", required = true, dataTypeClass = Long.class, paramType = "path")
//    })
    @Operation(
            summary = "Change manager",
            description = "It allows us to change a manager for the product, if it's acceptable"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeManagerOfProduct/{id}/{managerId}")
    public void changeManagerOfProduct(
            @PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
            @PathVariable(name = "managerId") @Parameter(description = "Identifier of the manager") long managerId) {
        managerService.changeManagerOfProduct(id, managerId);
    }

//    @ApiOperation(value = "Change status of the product")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Product with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "productId", value = "Id of the product", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "status", value = "New status for the product", required = true, dataTypeClass = Status.class, paramType = "path")
//    })
    @Operation(
            summary = "Change status of the product",
            description = "It allows us to change status of the product"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeStatusOfProduct/{id}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
                                      @PathVariable(name = "status") @Parameter(description = "New status for the product") Status status) {
        managerService.changeStatusOfProduct(id, status);
    }

//    @Api(value = "Change the limit for the product")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Product with id ... doesn't exist"),
//            @ApiResponse(code = 400, message = "Wrong type of provided data.")
//    })
//    @ApiParam
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "Id of the product", required = true, dataTypeClass = Long.class, paramType = "path"),
//            @ApiImplicitParam(name = "limitValue", value = "New value of the limit", required = true, dataTypeClass = Integer.class, paramType = "path")
//    })
    @Operation(
            summary = "Change limit",
            description = "It allows us to change the limit for the product"
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/changeLimitValueOfProduct/{id}/{limitValue}")
    public void changeLimitValueOfProduct(
            @PathVariable(name = "id") @Parameter(description = "Identifier of the product") long id,
            @PathVariable(name = "limitValue") @Parameter(description = "New value of the limit") int limitValue) {
        managerService.changeLimitValueOfProduct(id, limitValue);
    }

//    @ApiOperation(value = "Inactivate status of the product")
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "Access denied"),
//            @ApiResponse(code = 404, message = "Product with id ... doesn't exist")
//    })
//    @ApiImplicitParam(name = "id", value = "Id of the existing product", required = true, dataTypeClass = Long.class, paramType = "path")
    @Operation(
            summary = "Inactivate the product",
            description = "It allows us to change status of the product to 'INACTIVE'."
    )
    @PutMapping("/inactivateStatusOfProduct/{productId}")
    public void inactivateStatusOfProduct(@PathVariable(name = "productId") @Parameter(description = "Identifier of the product") long productId) {
        managerService.inactivateStatusOfProduct(productId);
    }
}
