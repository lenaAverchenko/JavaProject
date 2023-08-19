package telran.functionality.com.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "[012]", message = "The field status must be either 0 or 1")
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

    public Manager(String firstName, String lastName, List<Client> clients, List<Product> products, Timestamp updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = 1;
        this.clients = clients;
        this.products = products;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
