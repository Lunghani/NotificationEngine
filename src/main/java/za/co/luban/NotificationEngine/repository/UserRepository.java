package za.co.luban.NotificationEngine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.luban.NotificationEngine.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
