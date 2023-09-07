package telran.functionality.com.dto;
/**
 * Class AccountCreateDto - provided by user object, given to convert into Account entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Type;

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
    @Enumerated(EnumType.STRING)
    private Type type;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
}