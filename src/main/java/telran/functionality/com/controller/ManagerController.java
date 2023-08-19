package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ManagerCreateDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.service.ManagerService;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
public class ManagerController {

    @Autowired
    private final ManagerService managerService;
    @Autowired
    private final Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    @Autowired
    private final Converter<Product, ProductDto, ProductCreateDto> productConverter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);


    @GetMapping
    public List<ManagerDto> getAll() {
        logger.info("Call method getAll managers");
        List<ManagerDto> allManagers = managerService.getAll().stream()
                .map(manager -> managerConverter.toDto(manager))
                .collect(Collectors.toList());
        logger.info("Method getAll managers has resulted with: " + allManagers.toString());
        return allManagers;
    }

    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") long id) {
        logger.info("Call method getById {}", id);
        ManagerDto foundManager = managerConverter.toDto(managerService.getById(id));
        logger.info("Method getById {} result is: {}", id, foundManager);
        return foundManager;
    }

    @PostMapping
    public ManagerDto save(@RequestBody ManagerCreateDto managerCreateDto) {
        logger.info("Call method save manager {}", managerCreateDto);
        ManagerDto savedManager = managerConverter.toDto(managerService.create(managerConverter.toEntity(managerCreateDto)));
        logger.info("Method save manager has resulted with: {}", savedManager);
        return savedManager;
    }

    @PutMapping("/{id}")
    public ManagerDto updateInformation(@PathVariable(name = "id") long id, @RequestBody ManagerCreateDto managerCreateDto) {
        logger.info("Call method updateInformation about manager by id {} with information: {}", id, managerCreateDto);
        ManagerDto updatedManagerDto = managerConverter.toDto(managerService.update(id, managerConverter.toEntity(managerCreateDto)));
        logger.info("Method updateInformation has resulted with: {}", updatedManagerDto);
        return updatedManagerDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        logger.info("Call method delete manager by id {}", id);
        managerService.delete(id);
        logger.info("Method delete manager has ended");
    }

    @PutMapping("/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") long id, @PathVariable(name = "status") int status) {
        logger.info("Call method changeStatus of manager by id {}", id);
        managerService.changeStatus(id, status);
        logger.info("Method changeStatus has ended");
    }

    @PutMapping("/addProduct/{id}")
    public ManagerDto addProduct(@PathVariable(name = "id") long id, @RequestBody ProductCreateDto productDto) {
        logger.info("Call method addProduct: {} -  to manager by id {}", productDto, id);
        ManagerDto updatedManager = managerConverter.toDto(managerService.addProduct(id, productConverter.toEntity(productDto)));
        logger.info("Method addProduct has ended ith the result: {}", updatedManager);
        return updatedManager;
    }

    @PutMapping("/changeStatusOfProduct/{managerId}/{productId}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "managerId") long managerId, @PathVariable(name = "productId") long productId,
                                      @PathVariable(name = "status") int status) {
        logger.info("Call method changeStatusOfProduct for manager with id: {}; productId: {}, tpo set new status: {}", managerId, productId, status);
        managerService.changeStatusOfProduct(managerId, productId, status);
        logger.info("Method changeStatusOfProduct has ended");
    }

    @PutMapping("/changeManagerOfProduct/{id}/{managerId}")
    public void changeManagerOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "managerId") long managerId) {
        logger.info("Call method changeManagerOfProduct for product with id: {}", id);
        managerService.changeManagerOfProduct(id, managerId);
        logger.info("Method changeManagerOfProduct has ended");
    }

    @PutMapping("/changeStatusOfProduct/{id}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "status") int status) {
        logger.info("Call method changeStatusOfProduct for product with id: {}", id);
        managerService.changeStatusOfProduct(id, status);
        logger.info("Method changeStatusOfProduct has ended");
    }

    @PutMapping("/changeLimitValueOfProduct/{id}/{limitValue}")
    public void changeLimitValueOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "limitValue") int limitValue) {
        logger.info("Call method changeLimitValueOfProduct for product with id: {}", id);
        managerService.changeLimitValueOfProduct(id, limitValue);
        logger.info("Method changeLimitValueOfProduct has ended");
    }

    @PutMapping("/inactivateStatus/{id}/{limitValue}")
    public void inactivateStatus(@PathVariable(name = "id") long id) {
        logger.info("Call method inactivateStatus for manager with id: {}", id);
        managerService.inactivateStatus(id);
        logger.info("Method inactivateStatus has ended");
    }

    @PutMapping("/inactivateStatusOfProduct/{id}/{limitValue}")
    public void inactivateStatusOfProduct(@PathVariable(name = "id") long productId) {
        logger.info("Call method inactivateStatusOfProduct with id: {}", productId);
        managerService.inactivateStatusOfProduct(productId);
        logger.info("Method inactivateStatusOfProduct has ended");
    }

}
