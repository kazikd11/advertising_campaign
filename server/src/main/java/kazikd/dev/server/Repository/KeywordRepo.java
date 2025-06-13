package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepo extends JpaRepository<Keyword, Long> {
    List<Keyword> findByKeywordIgnoreCase(String keyword);
    List<Keyword> findByKeywordStartingWithIgnoreCase(String keyword);
}
