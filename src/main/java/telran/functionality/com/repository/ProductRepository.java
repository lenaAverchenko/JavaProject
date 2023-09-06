package telran.functionality.com.repository;
/**
 * Interface extending JpaRepository to get access to the Product entities
 * @see org.springframework.data.jpa.repository.JpaRepository
 *
 * @author Olena Averchenko
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.functionality.com.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
