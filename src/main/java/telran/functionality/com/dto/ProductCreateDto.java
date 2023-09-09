package telran.functionality.com.dto;
/**
 * Class ProductCreateDto - provided by user object, given to convert into Product entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import telran.functionality.com.enums.Currency;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreateDto {

    @Schema(description = "Manager identifier",defaultValue = "1")
    private long managerId;

    @Schema(description = "Name of product", defaultValue = "Mortgage")
    private String name;

    @Schema(description = "Currency",defaultValue = "USD")
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;

    @Schema(description = "Interest rate",defaultValue = "10.0")
    private double interestRate;

    @Schema(description = "Limit",defaultValue = "10000")
    private int limitValue;
}