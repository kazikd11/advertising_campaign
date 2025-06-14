package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.Product;

import java.math.BigDecimal;
import java.util.List;

public record UserDetailsDTO(Long id, String username, BigDecimal balance, List<ProductDTO> products) {
    public static UserDetailsDTO fromUserDetails(Long id, String username, BigDecimal balance, List<Product> products) {
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::fromProduct)
                .toList();
        return new UserDetailsDTO(id, username, balance, productDTOs);
    }
}
