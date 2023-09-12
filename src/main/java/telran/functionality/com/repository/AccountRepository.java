package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Account entities
 *
 * @author Olena Averchenko
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Account;

import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
