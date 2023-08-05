package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Account;

import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
