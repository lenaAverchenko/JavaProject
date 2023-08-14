package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Product;

@Component
public class ProductConverterImpl implements ProductConverter {
    @Autowired
    private ManagerConverter managerConverter;
    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), managerConverter.toDto(product.getManager()),
                product.getName(), product.getStatus(), product.getLimitValue());
    }

    @Override
    public Product toEntity(ProductDto productDto) {

        return new Product(productDto.getId(), managerConverter.toEntity(productDto.getManager()),
                productDto.getName(), productDto.getStatus(), productDto.getLimitValue());
    }
}
