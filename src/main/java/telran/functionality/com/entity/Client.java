package telran.functionality.com.entity;

import lombok.*;
import telran.functionality.com.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Manager manager;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Account> accounts = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Status status;
    private String taxCode;
    @NotBlank(message = "Field firstName is mandatory")
    private String firstName;
    @NotBlank(message = "Field lastName is mandatory")
    private String lastName;
    private String email;
    @NotBlank(message = "Field address is mandatory")
    private String address;
    private String phone;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());


    public Client(Manager manager, List<Account> accounts, String taxCode, String firstName, String lastName, String email, String address, String phone, Timestamp updatedAt) {
        this.manager = manager;
        this.accounts = accounts;
        this.status = Status.ACTIVE;
        this.taxCode = taxCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.updatedAt = updatedAt;
    }


    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
