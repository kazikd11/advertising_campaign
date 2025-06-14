package kazikd.dev.server.Controller;

import kazikd.dev.server.DTOs.ProductDTO;
import kazikd.dev.server.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader("X-USER-ID") Long userId,
                                                    @RequestParam String name) {
        ProductDTO product = productService.createProductForUser(userId, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("X-USER-ID") Long userId,
                                                @PathVariable Long productId) {
        productService.deleteProduct(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
    }
}
