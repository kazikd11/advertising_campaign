package kazikd.dev.server.Service;

import kazikd.dev.server.Exceptions.UserAlreadyExistsException;
import kazikd.dev.server.Exceptions.UserBalanceException;
import kazikd.dev.server.Exceptions.UserNotFoundException;
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

    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public void createUser(String name) {
        User user = new User();
        user.setUsername(name);
        try{
            userRepo.save(user);
        }
        catch(DataIntegrityViolationException e){
            throw new UserAlreadyExistsException("User with name " + name + " already exists");
        }
    }

    public User addFunds(Long userId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UserBalanceException("Amount must be greater than zero");
        }
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setBalance(user.getBalance().add(amount));
        return userRepo.save(user);
    }
}
