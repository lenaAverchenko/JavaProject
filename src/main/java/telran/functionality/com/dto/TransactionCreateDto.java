package telran.functionality.com.dto;


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
public class TransactionCreateDto {

    private UUID debitAccountId;
    private UUID creditAccountId;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double amount;
    private String description;
}
