package kazikd.dev.server.Controller;

import kazikd.dev.server.DTOs.CampaignRequestDTO;
import kazikd.dev.server.DTOs.CampaignResponseDTO;
import kazikd.dev.server.Service.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "*")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponseDTO>> getAllUserCampaigns(@RequestHeader("X-USER-ID") Long userId) {
        List<CampaignResponseDTO> campaigns = campaignService.getAllUserCampaigns(userId);
        return ResponseEntity.status(HttpStatus.OK).body(campaigns);
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDTO> createCampaign(@RequestHeader("X-USER-ID") Long userId, @RequestBody CampaignRequestDTO requestDTO) {
        CampaignResponseDTO campaign = campaignService.createCampaign(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaign);
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDTO> updateCampaign(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long campaignId, @RequestBody CampaignRequestDTO requestDTO) {
        CampaignResponseDTO updatedCampaign = campaignService.updateCampaign(userId, campaignId, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCampaign);
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<String> deleteCampaign(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long campaignId) {
        campaignService.deleteCampaign(userId, campaignId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Campaign deleted successfully");
    }
}
