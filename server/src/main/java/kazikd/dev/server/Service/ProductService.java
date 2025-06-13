package kazikd.dev.server.Service;

import kazikd.dev.server.Exceptions.ProductNotFoundException;
import kazikd.dev.server.Exceptions.UserNotFoundException;
import kazikd.dev.server.Model.Product;
import kazikd.dev.server.Model.User;
import kazikd.dev.server.Repository.ProductRepo;
import kazikd.dev.server.Repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    public ProductService(ProductRepo productRepo, UserRepo userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public void createProductForUser(Long userId, String name) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = new Product();
        product.setName(name);
        product.setUser(user);

        productRepo.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepo.delete(product);
    }

}
