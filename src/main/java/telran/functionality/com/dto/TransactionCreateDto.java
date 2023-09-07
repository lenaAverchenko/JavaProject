package telran.functionality.com.dto;
/**
 * Class TransactionCreateDto - provided by user object, given to convert into Transaction entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionCreateDto {

    private UUID debitAccountId;
    private UUID creditAccountId;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double amount;
    private String description;
}
