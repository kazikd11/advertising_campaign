package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.Campaign;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record CampaignResponseDTO(Long id, Long productId, String name, Set<KeywordDTO> keywords, BigDecimal bidAmount,
                                  BigDecimal fund, boolean isActive, TownDTO town, Integer radius) {
    public static CampaignResponseDTO fromCampaign(Campaign campaign) {
        return new CampaignResponseDTO(
            campaign.getId(),
            campaign.getProduct().getId(),
            campaign.getName(),
            campaign.getKeywords().stream().map(KeywordDTO::fromKeyword).collect(Collectors.toSet()),
            campaign.getBidAmount(),
            campaign.getFund(),
            campaign.isActive(),
            TownDTO.fromTown(campaign.getTown()),
            campaign.getRadius()
        );
    }
}
