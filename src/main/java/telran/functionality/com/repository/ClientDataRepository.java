package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the ClientData entities (with logins and passwords)
 *
 * @author Olena Averchenko
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.ClientData;


@Repository
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {

    ClientData findByLogin(String login);
}
