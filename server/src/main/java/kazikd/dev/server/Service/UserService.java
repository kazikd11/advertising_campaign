package kazikd.dev.server.Service;

import kazikd.dev.server.DTOs.UserDetailsDTO;
import kazikd.dev.server.Exceptions.UserAlreadyExistsException;
import kazikd.dev.server.Exceptions.UserException;
import kazikd.dev.server.Exceptions.NotFoundException;
import kazikd.dev.server.Model.User;
import kazikd.dev.server.DTOs.UserSummaryDTO;
import kazikd.dev.server.Repository.UserRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserSummaryDTO> getAllUsers() {
        return userRepo.findAll().stream().map(UserSummaryDTO::fromUser).toList();
    }

    public UserDetailsDTO getUserDetailsById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return UserDetailsDTO.fromUserDetails(user.getId(), user.getUsername(), user.getBalance(), user.getProducts());
    }

    public UserDetailsDTO createUser(String name) {
        User user = new User();
        user.setUsername(name);
        try {
            User saved = userRepo.save(user);
            return UserDetailsDTO.fromUserDetails(saved.getId(), saved.getUsername(), saved.getBalance(), saved.getProducts());
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with name " + name + " already exists");
        }
    }

    public UserDetailsDTO addFunds(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UserException("Amount must be greater than zero");
        }
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        user.setBalance(user.getBalance().add(amount));
        User saved = userRepo.save(user);
        return UserDetailsDTO.fromUserDetails(saved.getId(), saved.getUsername(), saved.getBalance(), saved.getProducts());
    }

}
