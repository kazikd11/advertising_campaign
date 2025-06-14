package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Long> {
    List<Campaign> findAllByProduct_User_Id(Long productUserId);
}
