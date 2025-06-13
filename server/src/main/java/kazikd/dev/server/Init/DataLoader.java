package kazikd.dev.server.Init;

import jakarta.annotation.PostConstruct;
import kazikd.dev.server.Model.Keyword;
import kazikd.dev.server.Model.Town;
import kazikd.dev.server.Repository.KeywordRepo;
import kazikd.dev.server.Repository.TownRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

//populating tables with initial data for towns and keywords
@Slf4j
@Component
public class DataLoader {
    private final TownRepo townRepo;
    private final KeywordRepo keywordRepo;

    public DataLoader(TownRepo townRepo, KeywordRepo keywordRepo) {
        this.townRepo = townRepo;
        this.keywordRepo = keywordRepo;
    }

    //loads every time for in a memory database. more useful for production
    @PostConstruct
    public void loadTowns() {
        if (townRepo.count() > 0) {
            log.info("Towns already loaded.");
            return;
        }
        loadFromTxtSaveToDB("/data/towns.txt", name -> {
            Town town = new Town();
            town.setName(name);
            return town;
        }, townRepo);
    }

    @PostConstruct
    public void loadKeywords() {
        if (keywordRepo.count() > 0) {
            log.info("Keywords already loaded.");
            return;
        }
        loadFromTxtSaveToDB("/data/keywords.txt", name -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(name);
            return keyword;
        }, keywordRepo);
    }

    //loads data from a text file and saves it to the database
    private <T> void loadFromTxtSaveToDB(String resourcePath, Function<String, T> mapper, JpaRepository<T, ?> repository) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                log.error("Resource not found: {}", resourcePath);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                List<T> items = reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .map(mapper)
                        .toList();
                repository.saveAll(items);
                log.info("Loaded {} entries from {}", items.size(), resourcePath);
            }
        } catch (Exception e) {
            log.error("Error reading resource {}: {}", resourcePath, e.getMessage());
        }
    }
}
