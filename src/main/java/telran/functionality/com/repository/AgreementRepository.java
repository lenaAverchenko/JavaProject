package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Agreement entities
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
