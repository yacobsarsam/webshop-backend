package org.example.webshop.Controller;

import org.example.webshop.Model.User;
import org.example.webshop.Repository.UserRepository;
import org.example.webshop.Security.JwtUtil;
import org.example.webshop.Services.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(400).body("User already exists"); // HTTP 400 (Bad Request)
        }
        User user = new User(null, username, passwordEncoder.encode(password), "ROLE_USER");
        userRepository.save(user);
        return ResponseEntity.status(201).body("User registered successfully"); // HTTP 201 (Created)
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login page"); // HTTP 200 (OK)
    }

    @GetMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Register page"); // HTTP 200 (OK)
    }
}
/*

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(String username, String password, Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "User already exists");
            return "register";
        }
        User user = new User(null, username, passwordEncoder.encode(password), "ROLE_USER");
        userRepository.save(user);
        return "redirect:/login";
    }
 */