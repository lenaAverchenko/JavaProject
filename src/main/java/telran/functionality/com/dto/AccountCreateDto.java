package telran.functionality.com.dto;

import lombok.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDto {

    private UUID clientId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;

}
