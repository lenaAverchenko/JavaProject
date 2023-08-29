package telran.functionality.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
