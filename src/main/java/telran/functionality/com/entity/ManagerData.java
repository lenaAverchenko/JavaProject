package telran.functionality.com.entity;
/**
 * Class ManagerData - to describe Manager's authorization data
 *
 * @author Olena Averchenko
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "managerdata")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ManagerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;
    private String password;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public ManagerData(String login, String password, Manager manager) {
        this.login = login;
        this.password = password;
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "ManagerData{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
