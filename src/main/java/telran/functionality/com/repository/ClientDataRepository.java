package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the ClientData entities (with logins and passwords)
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.ClientData;

import java.util.Optional;

@Repository
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {

   ClientData findByLogin(String login);
}
