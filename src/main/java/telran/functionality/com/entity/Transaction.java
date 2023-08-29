package telran.functionality.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString
@Data
@AllArgsConstructor
public class Transaction {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uniqueTransactionId;


//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account debitAccount;


//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
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
        this.uniqueTransactionId = UUID.randomUUID();
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

}
