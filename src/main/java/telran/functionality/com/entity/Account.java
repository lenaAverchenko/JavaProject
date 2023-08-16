package telran.functionality.com.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@ToString
@NoArgsConstructor
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
    private String name;
    private int type;
    private int status;
    private double balance;
    private int currencyCode;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;


    public Account(UUID id, String name, Client client, double balance) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.balance = balance;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
