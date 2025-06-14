package kazikd.dev.server.Service;

import kazikd.dev.server.DTOs.ProductDTO;
import kazikd.dev.server.Exceptions.NotFoundException;
import kazikd.dev.server.Exceptions.UserException;
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

    public ProductDTO createProductForUser(Long userId, String name) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = new Product();
        product.setName(name);
        product.setUser(user);

        return ProductDTO.fromProduct(productRepo.save(product));
    }


    public void deleteProduct(Long userId, Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!product.getUser().getId().equals(userId)) {
            throw new UserException("User does not own this product");
        }

        productRepo.delete(product);
    }

}
