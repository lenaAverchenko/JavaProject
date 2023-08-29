package telran.functionality.com.dto;

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
public class BalanceDto {

    private UUID idOfAccount;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
}
