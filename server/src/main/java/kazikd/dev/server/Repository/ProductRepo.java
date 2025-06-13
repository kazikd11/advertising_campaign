package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
