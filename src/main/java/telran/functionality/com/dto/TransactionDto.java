package telran.functionality.com.dto;
/**
 * Class TransactionDto - to create object from Transaction entity, which will be shown to user
 *
 * @author Olena Averchenko
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {


    @Schema(description = "Transaction identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837190000")
    private UUID uniqueId;

    @Schema(description = "Debit account identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837191111")
    private AccountDto debitAccount;

    @Schema(description = "Credit account identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837192222")
    private AccountDto creditAccount;

    @Schema(description = "Amount", defaultValue = "1000.00")
    private double amount;

    @Schema(description = "Time of creation", defaultValue = "2023-08-26T20:53:32.000+00:00")
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "TransactionDto{" +
                "uniqueId=" + uniqueId +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}