package kazikd.dev.server.Controller;

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
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
        List<UserSummaryDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestParam String username) {
        userService.createUser(username);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PutMapping("/{userId}/add-funds")
    public ResponseEntity<User> addFundsForUser(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(userService.addFunds(userId, amount));
    }
}