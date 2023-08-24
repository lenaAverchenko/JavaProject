package telran.functionality.com.entity;

import lombok.*;
import telran.functionality.com.enums.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Client client;
    @NotBlank(message = "Field name is mandatory")
    private String name;
    private int type;
    private int status;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());


    public Account(Client client, String name, int type, double balance, Currency currencyCode, Timestamp updatedAt) {
        this.client = client;
        this.name = name;
        this.type = type;
        this.status = 0;
        this.balance = balance;
        this.currencyCode = currencyCode;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
