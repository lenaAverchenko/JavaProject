package telran.functionality.com.dto;
/**
 * Class AccountCreateDto - provided by user object, given to convert into Account entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreateDto {

    @Schema(description = "Client identifier",defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837199fcc")
    private UUID clientId;

    @Schema(description = "Name of account",defaultValue = "My personal account")
    private String name;

    @Schema(description = "Type of account",defaultValue = "PERSONAL")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Schema(description = "Balance on the account",defaultValue = "200.00")
    private double balance;

    @Schema(description = "Currency",defaultValue = "USD")
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;

}