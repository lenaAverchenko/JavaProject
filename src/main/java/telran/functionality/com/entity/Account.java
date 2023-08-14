package telran.functionality.com.entity;

import lombok.Data;
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
    private UUID clientId;
    private String name;
    private int type;
    private int status;
    private double balance;
    private int currencyCode;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;

    public Account(UUID id, UUID clientId, String name, double balance) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.balance = balance;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
