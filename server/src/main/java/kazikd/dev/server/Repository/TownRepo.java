package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepo extends JpaRepository<Town, Long> {
}
