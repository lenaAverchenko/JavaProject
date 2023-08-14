package telran.functionality.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.entity.Account;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {
    private UUID id;
    private AccountDto debitAccount;
    private AccountDto creditAccount;
    private double amount;
    private Timestamp createdAt;
}
