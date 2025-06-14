package kazikd.dev.server.Controller;

import kazikd.dev.server.DTOs.KeywordDTO;
import kazikd.dev.server.DTOs.TownDTO;
import kazikd.dev.server.Service.LookupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// getting prepopulated data from the server - keywords (with typeahead) and towns
// simple implementation, could be extended with trigram/fuzzy search for type errors
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LookupController {

    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/towns")
    public ResponseEntity<List<TownDTO>> getTowns() {
        List<TownDTO> towns = lookupService.getAllTowns();
        return ResponseEntity.status(HttpStatus.OK).body(towns);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordDTO>> getKeywords(@RequestParam(required = false) String query) {
        List<KeywordDTO> keywords = lookupService.searchKeywords(query);
        return ResponseEntity.status(HttpStatus.OK).body(keywords);
    }

}