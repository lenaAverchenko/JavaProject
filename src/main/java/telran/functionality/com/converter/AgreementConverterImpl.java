package telran.functionality.com.converter;

/**
 * Class - Converter for the Agreement entities
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.ProductService;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AgreementConverterImpl implements Converter<Agreement, AgreementDto, AgreementCreateDto> {

    private final AccountService accountService;

    private final ProductService productService;
    /**
     * Method to convert Agreement entity to AgreementDto, showed to user.
     * @param agreement entity from database
     * @return AgreementDto object
     */
    @Override
    public AgreementDto toDto(Agreement agreement) {
        return new AgreementDto(
                new AccountDto(agreement.getAccount().getId(), agreement.getAccount().getName(),
                        agreement.getAccount().getStatus(),
                        new ClientDto(agreement.getAccount().getClient().getId(),
                                agreement.getAccount().getClient().getStatus(),
                                agreement.getAccount().getClient().getFirstName(),
                                agreement.getAccount().getClient().getLastName(), null, null)),
                new ProductDto(agreement.getProduct().getId(), agreement.getProduct().getName(),
                        agreement.getProduct().getStatus(), agreement.getProduct().getLimitValue(), null),
                agreement.getInterestRate(), agreement.getStatus(), agreement.getSum());
    }

    /**
     * Method to convert AgreementCreateDto (data provided by user) to entity Agreement, stored in database.
     * @param createdDto provided data
     * @return Agreement entity
     */
    @Override
    public Agreement toEntity(AgreementCreateDto createdDto) {
        return new Agreement(accountService.getById(createdDto.getAccountId()),
                productService.getById(createdDto.getProductId()),
                createdDto.getInterestRate(),
                createdDto.getSum(), new Timestamp(new Date().getTime()));
    }
}