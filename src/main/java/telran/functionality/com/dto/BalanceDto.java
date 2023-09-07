package telran.functionality.com.dto;
/**
 * Class BalanceDto - to create object as an answer for user to the request to show balance of the certain account
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
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

    private UUID idOfAccount;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
}