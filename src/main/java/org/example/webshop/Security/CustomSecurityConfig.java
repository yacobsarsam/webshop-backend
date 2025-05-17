package org.example.webshop.Security;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
@Configuration
public class CustomSecurityConfig {

    private final JwtFilter jwtFilter;

    public CustomSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll() // Public endpoints
                        .requestMatchers(HttpMethod.GET, "/products/**", "/category/**").permitAll() // Require authentication for DELETE
                        .requestMatchers(HttpMethod.POST, "/products/**", "/category/**").authenticated() // Require authentication for POST
                        .requestMatchers(HttpMethod.DELETE, "/products/**", "/category/**").authenticated() // Require authentication for DELETE
                        .requestMatchers(HttpMethod.PUT, "/products/**", "/category/**").authenticated()
                        .requestMatchers( "/users/**").hasRole("ADMIN") // Require authentication for DELETE// Require authentication for PUT
                        .requestMatchers("/auth/register").hasAuthority("ADMIN") // Admin-only endpoint
                        .anyRequest().authenticated() // Catch-all for other requests
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://webshop-backend-7ljm.onrender.com", "http://localhost:5173", "http://localhost:5174"));
        config.addExposedHeader("Authorization"); // if your token is sent in this header
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Only valid if NOT using wildcard origin
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}