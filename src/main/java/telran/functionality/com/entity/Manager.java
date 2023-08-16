package telran.functionality.com.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private int status;

    @OneToMany(mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products = new ArrayList<>();
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;

    public Manager(long id, String firstName, String lastName, List<Client> clients, List<Product> products) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clients = clients;
        this.products = products;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
