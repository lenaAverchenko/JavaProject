package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ManagerCreateDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;
import telran.functionality.com.service.ManagerService;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {


    private final ManagerService managerService;
    private final Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    private final Converter<Product, ProductDto, ProductCreateDto> productConverter;


    @GetMapping
    public List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") long id) {
        return managerConverter.toDto(managerService.getById(id));
    }

    @PostMapping
    public ManagerDto save(@RequestBody ManagerCreateDto managerCreateDto) {
        return managerConverter.toDto(managerService.save(managerConverter.toEntity(managerCreateDto)));
    }

    @PutMapping("/updateInformation/{id}")
    public ManagerDto updateInformation(@PathVariable(name = "id") long id, @RequestBody ManagerCreateDto managerCreateDto) {
        return managerConverter.toDto(managerService.update(id, managerConverter.toEntity(managerCreateDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        managerService.delete(id);
    }

    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable(name = "id") long id, @PathVariable(name = "status") Status status) {
        managerService.changeStatus(id, status);
    }

    @PutMapping("/inactivateStatus/{id}")
    public void inactivateStatus(@PathVariable(name = "id") long id) {
        managerService.inactivateStatus(id);
    }

    @PutMapping("/addProductTo/{id}")
    public ManagerDto addProduct(@PathVariable(name = "id") long id, @RequestBody ProductCreateDto productDto) {
        return managerConverter.toDto(managerService.addProduct(id, productConverter.toEntity(productDto)));
    }

    @PutMapping("/changeStatusOfProduct/{managerId}/{productId}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "managerId") long managerId, @PathVariable(name = "productId") long productId,
                                      @PathVariable(name = "status") Status status) {
        managerService.changeStatusOfProduct(managerId, productId, status);
    }

    @PutMapping("/changeManagerOfProduct/{id}/{managerId}")
    public void changeManagerOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "managerId") long managerId) {
        managerService.changeManagerOfProduct(id, managerId);
    }

    @PutMapping("/changeStatusOfProduct/{id}/{status}")
    public void changeStatusOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "status") Status status) {
        managerService.changeStatusOfProduct(id, status);
    }

    @PutMapping("/changeLimitValueOfProduct/{id}/{limitValue}")
    public void changeLimitValueOfProduct(@PathVariable(name = "id") long id, @PathVariable(name = "limitValue") int limitValue) {
        managerService.changeLimitValueOfProduct(id, limitValue);
    }

    @PutMapping("/inactivateStatusOfProduct/{id}")
    public void inactivateStatusOfProduct(@PathVariable(name = "id") long productId) {
        managerService.inactivateStatusOfProduct(productId);
    }

}
