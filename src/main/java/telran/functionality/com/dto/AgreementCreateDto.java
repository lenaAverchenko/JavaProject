package telran.functionality.com.dto;
/**
 * Class AgreementCreateDto - provided by user object, given to convert into Agreement entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementCreateDto {

    @Schema(description = "Account identifier",defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837190000")
    private UUID accountId;

    @Schema(description = "Product identifier",defaultValue = "1")
    private long productId;

    @Schema(description = "Interest rate",defaultValue = "1.0")
    private double interestRate;

    @Schema(description = "Sum",defaultValue = "1000.00")
    private double sum;
}
