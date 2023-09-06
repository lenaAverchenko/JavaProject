package telran.functionality.com.entity;
/**
 * Class ManagerData - to describe Manager's authorization data
 *
 * @author Olena Averchenko
 * */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "managerdata")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class ManagerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;
    private String password;
//    private Role role;
//    private long managerId;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public ManagerData(String login, String password, Manager manager) {
        this.login = login;
        this.password = password;
//        this.role = new Role("ADMIN");
        this.manager = manager;
    }
}
