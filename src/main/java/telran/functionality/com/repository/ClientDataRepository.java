package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.ClientData;

@Repository
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {

    ClientData findByLogin(String login);
}
