package telran.functionality.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Transaction;

import java.util.UUID;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
