package kazikd.dev.server.Controller;

import kazikd.dev.server.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestHeader("X-USER-ID") Long userId,
                                                @RequestParam String name) {
        productService.createProductForUser(userId, name);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("X-USER-ID") Long userId,
                                                @PathVariable Long productId) {
        productService.deleteProduct(userId ,productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
