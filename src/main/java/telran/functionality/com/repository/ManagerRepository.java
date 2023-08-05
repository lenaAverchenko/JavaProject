package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
