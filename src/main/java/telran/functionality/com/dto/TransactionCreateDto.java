package telran.functionality.com.dto;
/**
 * Class TransactionCreateDto - provided by user object, given to convert into Transaction entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.enums.Type;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionCreateDto {

    @Schema(description = "Debit account identifier",defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837191111")
    private UUID debitAccountId;

    @Schema(description = "Credit account identifier",defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837192222")
    private UUID creditAccountId;

    @Schema(description = "Type of account",defaultValue = "PERSONAL")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Schema(description = "Amount",defaultValue = "1000.00")
    private double amount;

    @Schema(description = "Description",defaultValue = "Internal transfer")
    private String description;
}
