package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.Product;

public record ProductDTO(Long id, String name) {
    public static ProductDTO fromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getName());
    }
}
