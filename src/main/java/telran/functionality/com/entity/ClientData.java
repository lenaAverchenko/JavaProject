package telran.functionality.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "clientdata")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class ClientData {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String login;
    private String password;
//    private Role role;
//    private UUID clientId;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;


    public ClientData(String login, String password, Client client) {
        this.login = login;
        this.password = password;
//        this.role = new Role("USER");
        this.client = client;
    }
}
