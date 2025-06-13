package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepo extends JpaRepository<Keyword, Long> {
}
