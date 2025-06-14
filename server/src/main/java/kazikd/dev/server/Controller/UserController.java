package kazikd.dev.server.Controller;

import kazikd.dev.server.DTOs.UserDetailsDTO;
import kazikd.dev.server.Model.User;
import kazikd.dev.server.DTOs.UserSummaryDTO;
import kazikd.dev.server.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//demo controller, no authentication, no security, just to use user_id in other requests
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
        List<UserSummaryDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping
    public ResponseEntity<UserDetailsDTO> createUser(@RequestParam String username) {
        UserDetailsDTO user = userService.createUser(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsDTO> getCurrentUserAndData(@RequestHeader("X-USER-ID") Long userId) {
        UserDetailsDTO user = userService.getUserDetailsById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/add-funds")
    public ResponseEntity<UserDetailsDTO> addFunds(@RequestHeader("X-USER-ID") Long userId,
                                                   @RequestParam BigDecimal amount) {
        UserDetailsDTO user = userService.addFunds(userId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}