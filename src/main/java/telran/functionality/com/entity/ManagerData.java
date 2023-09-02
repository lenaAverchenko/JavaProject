package telran.functionality.com.entity;

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
    private long managerId;

    public ManagerData(String login, String password, long managerId) {
        this.login = login;
        this.password = password;
//        this.role = new Role("ADMIN");
        this.managerId = managerId;
    }
}
