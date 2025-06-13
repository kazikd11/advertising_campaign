package kazikd.dev.server.Controller;

import kazikd.dev.server.Model.User;
import kazikd.dev.server.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//demo controller, no authentication, no security, just to use hardcoded user_id
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
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
