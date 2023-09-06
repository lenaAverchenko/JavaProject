package telran.functionality.com.dto;
/**
 * Class TransactionDto - to create object from Transaction entity, which will be shown to user
 *
 * @author Olena Averchenko
 * */
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private UUID uniqueId;
    private AccountDto debitAccount;
    private AccountDto creditAccount;
    private double amount;
    private Timestamp createdAt;
}