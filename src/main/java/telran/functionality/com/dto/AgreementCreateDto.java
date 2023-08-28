package telran.functionality.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AgreementCreateDto {

    private UUID accountId;
    private long productId;
    private double interestRate;
    private double sum;
}
