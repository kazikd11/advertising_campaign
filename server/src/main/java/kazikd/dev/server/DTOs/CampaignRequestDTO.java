package kazikd.dev.server.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

// DTO for creating or updating a campaign
@Data
public class CampaignRequestDTO {
    @NotNull
    private Long productId;

    @NotBlank
    private String name;

    @Size(min = 1)
    private Set<Long> keywordIds;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal bidAmount;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal fund;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Long townId;

    @NotNull
    @Min(1)
    private Integer radius;
}
