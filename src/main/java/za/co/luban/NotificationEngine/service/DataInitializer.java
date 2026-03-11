package za.co.luban.NotificationEngine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.co.luban.NotificationEngine.model.User;
import za.co.luban.NotificationEngine.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) { // only insert if empty
            User admin = new User();
            admin.setUsername("admin");
            admin.setUserPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setNotify(true);

            User user = new User();
            user.setUsername("user");
            user.setUserPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setNotify(false);

            userRepository.save(admin);
            userRepository.save(user);

            System.out.println("Inserted default users");
        }
    }
}