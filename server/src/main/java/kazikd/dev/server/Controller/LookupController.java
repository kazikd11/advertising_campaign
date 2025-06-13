package kazikd.dev.server.Controller;

import kazikd.dev.server.DTOs.KeywordDTO;
import kazikd.dev.server.Model.Keyword;
import kazikd.dev.server.Model.Town;
import kazikd.dev.server.Service.LookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// getting prepopulated data from the server - keywords (with typeahead) and towns
// simple implementation, could be extended with trigram/fuzzy search for type errors
@RestController
@RequestMapping("/api")
public class LookupController {

    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/towns")
    public ResponseEntity<List<Town>> getTowns() {
        List<Town> towns = lookupService.getAllTowns();
        return ResponseEntity.ok(towns);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordDTO>> getKeywords(@RequestParam(required = false) String query) {
        List<KeywordDTO> keywords = lookupService.searchKeywords(query);
        return ResponseEntity.ok(keywords);
    }
}