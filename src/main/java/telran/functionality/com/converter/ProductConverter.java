package telran.functionality.com.converter;

import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Product;

public interface ProductConverter {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
