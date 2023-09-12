package telran.functionality.com.converter;

/**
 * Class - Converter for the Manager entities
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Product;
import telran.functionality.com.service.ManagerService;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProductConverterImpl implements Converter<Product, ProductDto, ProductCreateDto> {

    private final ManagerService managerService;

    /**
     * Method to convert Product entity to ProductDto, showed to user.
     *
     * @param product entity from database
     * @return ProductDto object
     */
    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(),
                product.getStatus(), product.getLimitValue(),
                new ManagerDto(product.getManager().getId(),
                        product.getManager().getFirstName(),
                        product.getManager().getLastName(),
                        product.getManager().getStatus(), null, null));
    }

    /**
     * Method to convert ProductCreateDto (data provided by user) to entity Product, stored in database.
     *
     * @param createdDto provided data
     * @return Product entity
     */
    @Override
    public Product toEntity(ProductCreateDto createdDto) {
        return new Product(managerService.getById(createdDto.getManagerId()),
                createdDto.getName(), createdDto.getCurrencyCode(),
                createdDto.getInterestRate(), createdDto.getLimitValue(), new Timestamp(new Date().getTime()));
    }
}