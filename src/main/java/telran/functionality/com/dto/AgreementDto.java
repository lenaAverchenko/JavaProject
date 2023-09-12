package telran.functionality.com.dto;
/**
 * Class AgreementDto - to create object from Agreement entity, which will be shown to user
 *
 * @author Olena Averchenko
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.functionality.com.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDto {

    @Schema(description = "Account identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837190000")
    private AccountDto accountDto;

    @Schema(description = "Product identifier", defaultValue = "1")
    private ProductDto productDto;

    @Schema(description = "Interest rate", defaultValue = "1.0")
    private double interestRate;

    @Schema(description = "Status of the agreement", defaultValue = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Sum", defaultValue = "1000.00")
    private double sum;

    @Override
    public String toString() {
        return "AgreementDto{" +
                ", interestRate=" + interestRate +
                ", status=" + status +
                ", sum=" + sum +
                '}';
    }
}