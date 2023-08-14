package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.ManagerConverter;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.service.ManagerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerConverter managerConverter;

    @GetMapping
    public List<ManagerDto> getAll() {

        return managerService.getAll().stream()
                .map(manager -> managerConverter.toDto(manager))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") long id) {

        return managerConverter.toDto(managerService.getById(id));
    }

    @PostMapping
    public ManagerDto save(@RequestBody ManagerDto managerDto) {

        return managerConverter.toDto(managerService.create(managerConverter.toEntity(managerDto)));
    }
    @PutMapping("/{id}")
    public ManagerDto updateInformation(@PathVariable(name = "id") long id, @RequestBody ManagerDto managerDto){
        return managerConverter.toDto(managerService.update(id, managerConverter.toEntity(managerDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        managerService.delete(id);
    }
}
