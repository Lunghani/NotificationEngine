package za.co.luban.NotificationEngine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.co.luban.NotificationEngine.model.User;
import za.co.luban.NotificationEngine.repository.UserRepository;
import za.co.luban.NotificationEngine.utility.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get(USERNAME);
        String password = payload.get(PASSWORD);

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getUserPassword()))
                .map(user -> {
                    String token = JwtUtil.generateToken(username);
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error:","Invalid credentials")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String username = payload.get(USERNAME);
        String password = passwordEncoder.encode(payload.get(PASSWORD));

        User user = new User();
        user.setUsername(username);
        user.setUsername(password);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
