package telran.functionality.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account debitAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account creditAccount;

    private int type;
    private double amount;
    private String description;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());

    public Transaction(UUID id, Account debitAccount, Account creditAccount, double amount) {
        this.id = id;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    public Transaction(Account debitAccount, Account creditAccount, int type, double amount, String description) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }
}
