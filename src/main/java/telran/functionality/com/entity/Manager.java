package telran.functionality.com.entity;
/**
 * Class Manager - to describe a manager
 *
 * @author Olena Averchenko
 */
import lombok.*;
import telran.functionality.com.enums.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "managers")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products = new ArrayList<>();
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    public Manager(String firstName, String lastName, List<Client> clients, List<Product> products, Timestamp updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = Status.ACTIVE;
        this.clients = clients;
        this.products = products;
        this.updatedAt = updatedAt;
    }

    public Manager(long id, String firstName, String lastName, List<Client> clients, List<Product> products, Timestamp updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = Status.ACTIVE;
        this.clients = clients;
        this.products = products;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
