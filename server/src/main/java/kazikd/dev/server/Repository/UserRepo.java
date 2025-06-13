package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
