package telran.functionality.com.dto;
/**
 * Class BalanceDto - to create object as an answer for user to the request to show balance of the certain account
 *
 * @author Olena Averchenko
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.enums.Currency;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceDto {

    @Schema(description = "Account identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837190000")
    private UUID idOfAccount;

    @Schema(description = "Sum", defaultValue = "1000.00")
    private double balance;

    @Schema(description = "Currency", defaultValue = "USD")
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
}