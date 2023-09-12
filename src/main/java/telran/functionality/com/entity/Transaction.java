package telran.functionality.com.entity;
/**
 * Class Transaction - to describe a transactions
 *
 * @author Olena Averchenko
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.functionality.com.enums.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account debitAccount;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account creditAccount;

    @Enumerated(EnumType.STRING)
    private Type type;
    @Positive
    private double amount;
    @NotBlank(message = "Field description is mandatory")
    private String description;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());

    public Transaction(Account debitAccount, Account creditAccount, Type type, double amount, String description) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
