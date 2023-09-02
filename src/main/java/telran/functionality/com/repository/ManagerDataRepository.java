package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.ManagerData;

@Repository
public interface ManagerDataRepository extends JpaRepository<ManagerData, Long> {

    ManagerData findByLogin(String login);
}
