package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
