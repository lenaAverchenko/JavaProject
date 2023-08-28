package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Product;
import telran.functionality.com.service.ManagerService;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class ProductConverterImpl implements Converter<Product, ProductDto, ProductCreateDto> {


    @Autowired
    private ManagerService managerService;

    @Override
    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto(product.getId(), product.getName(),
                product.getStatus(), product.getLimitValue(),
                new ManagerDto(product.getManager().getId(),
                        product.getManager().getFirstName(),
                        product.getManager().getLastName(),
                        product.getManager().getStatus(), null, null));
        return productDto;
    }


    @Override
    public Product toEntity(ProductCreateDto createdDto) {
        Product product = new Product(managerService.getById(createdDto.getManagerId()),
                createdDto.getName(), createdDto.getCurrencyCode(),
                createdDto.getInterestRate(), createdDto.getLimitValue(), new Timestamp(new Date().getTime()));
        return product;
    }
}
