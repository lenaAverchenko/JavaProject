package telran.functionality.com.converter;

import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;

@Component
public class ProductConverterImpl implements Converter<Product, ProductDto> {

    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(),
                product.getStatus(), product.getLimitValue(),
                new ManagerDto(product.getManager().getId(),
                        product.getManager().getFirstName(),
                        product.getManager().getLastName(), null, null));
    }

    @Override
    public Product toEntity(ProductDto productDto) {

        return new Product(productDto.getId(), productDto.getName(),
                productDto.getStatus(), productDto.getLimitValue(),
                new Manager(productDto.getManager().getId(),
                        productDto.getManager().getFirstName(),
                        productDto.getManager().getLastName(), null, null));
    }
}
