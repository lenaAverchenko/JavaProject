package telran.functionality.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ManagerCreateDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.ManagerData;
import telran.functionality.com.entity.Product;
import telran.functionality.com.enums.Status;
import telran.functionality.com.service.ManagerDataService;
import telran.functionality.com.service.ManagerService;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

    @Autowired
    private PasswordEncoder encoder;
    private final ManagerService managerService;
    private final Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    private final Converter<Product, ProductDto, ProductCreateDto> productConverter;
    private final ManagerDataService managerDataService;


    @GetMapping
    public List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/showManager")
    public ManagerDto getById(@RequestParam long id) {
        return managerConverter.toDto(managerService.getById(id));
    }

    @PostMapping
    public ManagerDto save(@RequestBody ManagerCreateDto managerCreateDto) {
        Manager manager = managerConverter.toEntity(managerCreateDto);
        managerDataService.create(new ManagerData(
                managerCreateDto.getLogin(),
                encoder.encode(managerCreateDto.getPassword()),
                manager));
        return managerConverter.toDto(managerService.save(manager));
    }

    @PutMapping("/updateInformation/{id}")
    public ManagerDto updateInformation(@PathVariable(name = "id") long id, @RequestBody ManagerCreateDto managerCreateDto) {
        return managerConverter.toDto(managerService.update(id, managerConverter.toEntity(managerCreateDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        managerService.delete(id);
    }

    @PutMapping("/changeStatus")
    public void changeStatus(@RequestParam long id, @RequestParam Status status) {
        managerService.changeStatus(id, status);
    }

    @PutMapping("/inactivateStatus")
    public void inactivateStatus(@RequestParam long id) {
        managerService.inactivateStatus(id);
    }

    @PutMapping("/addProductTo/{id}")
    public ManagerDto addProduct(@PathVariable(name = "id") long id, @RequestBody ProductCreateDto productDto) {
        return managerConverter.toDto(managerService.addProduct(id, productConverter.toEntity(productDto)));
    }

    @PutMapping("/changeStatusOfProductForManager")
    public void changeStatusOfProduct(@RequestParam long managerId, @RequestParam long productId,
                                      @RequestParam Status status) {
        managerService.changeStatusOfProduct(managerId, productId, status);
    }

    @PutMapping("/changeManagerOfProduct")
    public void changeManagerOfProduct(@RequestParam long id, @RequestParam long managerId) {
        managerService.changeManagerOfProduct(id, managerId);
    }

    @PutMapping("/changeStatusOfProduct")
    public void changeStatusOfProduct(@RequestParam long id, @RequestParam Status status) {
        managerService.changeStatusOfProduct(id, status);
    }

    @PutMapping("/changeLimitValueOfProduct")
    public void changeLimitValueOfProduct(@RequestParam long id, @RequestParam int limitValue) {
        managerService.changeLimitValueOfProduct(id, limitValue);
    }

    @PutMapping("/inactivateStatusOfProduct")
    public void inactivateStatusOfProduct(@RequestParam long productId) {
        managerService.inactivateStatusOfProduct(productId);
    }

}
