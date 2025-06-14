package kazikd.dev.server.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    private String name;

    @ManyToMany
    @JoinTable(
            name = "campaign_keywords",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    @Size(min = 1)
    private Set<Keyword> keywords = new HashSet<>();

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal bidAmount;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal fund;

    private boolean isActive = false;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    @NotNull
    @Min(1)
    private Integer radius;

}