package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the ManagerData entities (with logins and passwords)
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.ManagerData;

@Repository
public interface ManagerDataRepository extends JpaRepository<ManagerData, Long> {

    ManagerData findByLogin(String login);
}
