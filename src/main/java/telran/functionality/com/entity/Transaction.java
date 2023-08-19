package telran.functionality.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //    @OneToOne(cascade = CascadeType.ALL)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account debitAccount;

    //    @OneToOne(cascade = CascadeType.ALL)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account creditAccount;

    private int type;
    @NotBlank(message = "Field description is mandatory")
    @Positive
    private double amount;
    @NotBlank(message = "Field description is mandatory")
    private String description;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());

    public Transaction(Account debitAccount, Account creditAccount, int type, double amount, String description) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }
}
