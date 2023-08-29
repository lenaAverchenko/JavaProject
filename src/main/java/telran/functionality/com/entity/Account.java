package telran.functionality.com.entity;

import lombok.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Client client;
    @NotBlank(message = "Field name is mandatory")
    private String name;


    @OneToMany(mappedBy = "debitAccount", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> debitTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "creditAccount", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> creditTransactions = new ArrayList<>();

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Agreement agreement;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());


    public Account(Client client, String name, Type type, double balance, Currency currencyCode, Timestamp updatedAt) {
        this.client = client;
        this.name = name;
        this.type = type;
        this.status = Status.INACTIVE;
        this.balance = balance;
        this.currencyCode = currencyCode;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
