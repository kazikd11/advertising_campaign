package kazikd.dev.server.Service;

import kazikd.dev.server.DTOs.KeywordDTO;
import kazikd.dev.server.DTOs.TownDTO;
import kazikd.dev.server.Model.Keyword;
import kazikd.dev.server.Repository.KeywordRepo;
import kazikd.dev.server.Repository.TownRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LookupService {

    private final TownRepo townRepo;
    private final KeywordRepo keywordRepo;

    public LookupService(TownRepo townRepo, KeywordRepo keywordRepo) {
        this.townRepo = townRepo;
        this.keywordRepo = keywordRepo;
    }

    public List<TownDTO> getAllTowns() {
        return townRepo.findAll().stream()
                .map(TownDTO::fromTown)
                .toList();
    }

    // searches for towns with a query, first by exact match, then by starting with
    public List<KeywordDTO> searchKeywords(String query) {
        List<Keyword> result;
        if (query == null || query.isBlank()) {
            result = keywordRepo.findAll();
        } else {
            result = new ArrayList<>();
            result.addAll(keywordRepo.findByKeywordIgnoreCase(query));
            result.addAll(keywordRepo.findByKeywordStartingWithIgnoreCase(query));
            result = result.stream().distinct().toList();
        }
        return result.stream()
                .map(KeywordDTO::fromKeyword)
                .toList();
    }

}
