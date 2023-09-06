package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Manager entities
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
