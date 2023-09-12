package telran.functionality.com.entity;
/**
 * Class ClientData - to describe Client's authorization data
 *
 * @author Olena Averchenko
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "clientdata")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;
    private String password;


    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;


    public ClientData(String login, String password, Client client) {
        this.login = login;
        this.password = password;
        this.client = client;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
