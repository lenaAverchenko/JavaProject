package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.service.ManagerServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;

    @GetMapping
    public List<Manager> getAll() {
        return managerService.getAll();
    }

    @GetMapping("/{id}")
    public Manager getById(@PathVariable(name = "id") long id) {
        return managerService.getById(id);
    }

    @PostMapping
    public Manager save(@RequestBody Manager manager){
        return managerService.create(manager);
    }
}
