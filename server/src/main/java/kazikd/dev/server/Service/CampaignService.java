package kazikd.dev.server.Service;

import jakarta.persistence.EntityManager;
import kazikd.dev.server.DTOs.CampaignRequestDTO;
import kazikd.dev.server.DTOs.CampaignResponseDTO;
import kazikd.dev.server.Exceptions.NotFoundException;
import kazikd.dev.server.Exceptions.UserException;
import kazikd.dev.server.Model.*;
import kazikd.dev.server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// manages crud for campaigns
@Service
public class CampaignService {
    private final CampaignRepo campaignRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final TownRepo townRepo;
    private final KeywordRepo keywordRepo;

    @Autowired
    private EntityManager entityManager;

    public CampaignService(CampaignRepo campaignRepo, ProductRepo productRepo, UserRepo userRepo, TownRepo townRepo, KeywordRepo keywordRepo) {
        this.campaignRepo = campaignRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.townRepo = townRepo;
        this.keywordRepo = keywordRepo;
    }

    public List<CampaignResponseDTO> getAllUserCampaigns(Long userId) {
        return campaignRepo.findAllByProduct_User_Id(userId).stream()
                .map(CampaignResponseDTO::fromCampaign)
                .toList();
    }

    @Transactional
    public void deleteCampaign(Long userId, Long campaignId) {
        Campaign campaign = getAndValidateCampaign(campaignId, userId);

        User user = campaign.getProduct().getUser();
        user.setBalance(user.getBalance().add(campaign.getFund()));
        userRepo.save(user);

        // detach the campaign from the product before deleting
        Product product = campaign.getProduct();
        product.setCampaign(null);
        productRepo.save(product);

        campaignRepo.delete(campaign);
    }

    @Transactional
    public CampaignResponseDTO createCampaign(Long userId, CampaignRequestDTO requestDTO) {
        Product product = productRepo.findById(requestDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (!product.getUser().getId().equals(userId)) {
            throw new UserException("User does not own this product");
        }

        User user = product.getUser();

        validateAndSaveFundChange(user, requestDTO.getFund());
        Town town = getTownById(requestDTO.getTownId());
        Set<Keyword> keywords = getKeywordsByIds(requestDTO.getKeywordIds());

        Campaign campaign = new Campaign();
        campaign.setProduct(product);
        Campaign saved = populateAndSaveCampaign(requestDTO, town, keywords, campaign);
        return CampaignResponseDTO.fromCampaign(saved);
    }

    @Transactional
    public CampaignResponseDTO updateCampaign(Long userId, Long campaignId, CampaignRequestDTO requestDTO) {
        Campaign campaign = getAndValidateCampaign(campaignId, userId);

        User user = campaign.getProduct().getUser();
        user.setBalance(user.getBalance().add(campaign.getFund()));

        validateAndSaveFundChange(user, requestDTO.getFund());
        Town town = getTownById(requestDTO.getTownId());
        Set<Keyword> keywords = getKeywordsByIds(requestDTO.getKeywordIds());

        Campaign updated = populateAndSaveCampaign(requestDTO, town, keywords, campaign);
        return CampaignResponseDTO.fromCampaign(updated);
    }

    //dry methods

    // populates the campaign with data from the request DTO and saves it to the repo
    private Campaign populateAndSaveCampaign(CampaignRequestDTO requestDTO, Town town, Set<Keyword> keywords, Campaign campaign) {
        campaign.setName(requestDTO.getName());
        campaign.setKeywords(keywords);
        campaign.setBidAmount(requestDTO.getBidAmount());
        campaign.setFund(requestDTO.getFund());
        campaign.setActive(requestDTO.getIsActive());
        campaign.setTown(town);
        campaign.setRadius(requestDTO.getRadius());
        return campaignRepo.save(campaign);
    }

    // validates user balance and updates it - deducts the fund amount from the user's balance
    private void validateAndSaveFundChange(User user, BigDecimal fund) {
        if (user.getBalance().compareTo(fund) < 0) {
            throw new UserException("Insufficient funds");
        }

        user.setBalance(user.getBalance().subtract(fund));
        userRepo.save(user);
    }

    private Town getTownById(Long townId) {
        return townRepo.findById(townId)
                .orElseThrow(() -> new NotFoundException("Town not found"));
    }

    private Set<Keyword> getKeywordsByIds(Set<Long> keywordIds) {
        Set<Keyword> keywords = new HashSet<>(keywordRepo.findAllById(keywordIds));
        if (keywords.size() != keywordIds.size()) {
            throw new NotFoundException("One or more keywords not found");
        }
        return keywords;
    }

    // retrieves the campaign by ID and validates that the user owns the campaign's product
    private Campaign getAndValidateCampaign(Long campaignId, Long userId) {
        Campaign campaign = campaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found"));

        if (!campaign.getProduct().getUser().getId().equals(userId)) {
            throw new UserException("User does not own this campaign");
        }

        return campaign;
    }
}
