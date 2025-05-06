package org.example.webshop.Controller;

import org.example.webshop.Dtos.LoginDto;
import org.example.webshop.Model.Role;
import org.example.webshop.Model.User;
import org.example.webshop.Repository.UserRepository;
import org.example.webshop.Security.JwtUtil;
import org.example.webshop.Services.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String email, @RequestParam String password, @RequestParam Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(400).body("User already exists"); // HTTP 400 (Bad Request)
        }
        User user = new User(null, email, passwordEncoder.encode(password), role);
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Generate JWT using email
            String token = jwtUtil.generateToken(email);

            // Retrieve the user's role
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Role not found"))
                    .getAuthority();
            // Return token and role
            return ResponseEntity.ok(Map.of("token", token, "role", role));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}