package telran.functionality.com.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.ProductService;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class AgreementConverterImpl implements Converter<Agreement, AgreementDto, AgreementCreateDto> {

    private static final Logger logger = LoggerFactory.getLogger(AgreementConverterImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductService productService;

    @Override
    public AgreementDto toDto(Agreement agreement) {
        logger.debug("Call method toDto for Agreement: {}", agreement);
        AgreementDto newAgreementDto = new AgreementDto(
                new AccountDto(agreement.getAccount().getId(), agreement.getAccount().getName(),
                        agreement.getAccount().getStatus(),
                        new ClientDto(agreement.getAccount().getClient().getId(),
                                agreement.getAccount().getClient().getStatus(),
                                agreement.getAccount().getClient().getFirstName(),
                                agreement.getAccount().getClient().getLastName(), null, null),
                        agreement.getAccount().getBalance()),
                new ProductDto(agreement.getProduct().getId(), agreement.getProduct().getName(),
                        agreement.getProduct().getStatus(), agreement.getProduct().getLimitValue(), null),
                agreement.getInterestRate(), agreement.getStatus(), agreement.getSum());
        logger.debug("Method toDto has ended with the result: {}", newAgreementDto);
        return newAgreementDto;
    }

    @Override
    public Agreement toEntity(AgreementCreateDto createdDto) {
        logger.debug("Call method toEntity for Agreement: {}", createdDto);
        Agreement agreement = new Agreement(accountService.getById(createdDto.getAccountId()),
                productService.getById(createdDto.getProductId()),
                createdDto.getInterestRate(),
                createdDto.getSum(), new Timestamp(new Date().getTime()));
        logger.debug("Method toEntity has ended with the result: {}", agreement);
        return agreement;
    }
}
