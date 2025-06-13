package kazikd.dev.server.Controller;

import kazikd.dev.server.Model.Product;
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
    public ResponseEntity<String> createProduct(@RequestParam Long userId, @RequestParam String name) {
        productService.createProductForUser(userId, name);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
