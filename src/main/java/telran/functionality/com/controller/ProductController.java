package telran.functionality.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.entity.Product;
import telran.functionality.com.service.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable(name = "id") long id) {
        return productService.getById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product){
        return productService.create(product);
    }
}
