package telran.functionality.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import telran.functionality.com.enums.Currency;

import javax.persistence.*;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreateDto {

    private UUID clientId;
    private String name;
    private int type;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
}
