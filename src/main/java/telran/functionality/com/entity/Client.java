package telran.functionality.com.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@ToString
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Manager manager;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id_")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Account> accounts;
    private int status;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;


    public Client(UUID id, Manager manager, List<Account> accounts, String firstName, String lastName) {
        this.id = id;
        this.manager = manager;
        this.accounts = accounts;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
