package org.example.webshop.Controller;

import org.example.webshop.Model.Role;
import org.example.webshop.Model.User;
import org.example.webshop.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestParam String email,
                                             @RequestParam String password,
                                             @RequestParam Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(400).body("User already exists");
        }

        User user = new User(null, email, passwordEncoder.encode(password), role);
        userRepository.save(user);
        return ResponseEntity.status(201).body("User created successfully");
    }
}