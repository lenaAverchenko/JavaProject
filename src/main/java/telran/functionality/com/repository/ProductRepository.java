package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Product entities
 *
 * @author Olena Averchenko
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
