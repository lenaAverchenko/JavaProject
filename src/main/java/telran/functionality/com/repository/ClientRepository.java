package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Client entities
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Client;

import java.util.UUID;


@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
}
