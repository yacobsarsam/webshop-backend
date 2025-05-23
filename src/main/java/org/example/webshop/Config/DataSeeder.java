package org.example.webshop.Config;

import org.example.webshop.Model.Category;
import org.example.webshop.Model.Product;
import org.example.webshop.Model.Role;
import org.example.webshop.Model.User;
import org.example.webshop.Repository.CategoryRepository;
import org.example.webshop.Repository.ProductRepository;
import org.example.webshop.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedDatabase(CategoryRepository categoryRepository, ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Add initial categories
            if (categoryRepository.count() == 0) {
                Category electronics = categoryRepository.save(new Category(null, "Electronics"));
                Category clothing = categoryRepository.save(new Category(null, "Clothing"));

                // Add initial products
                productRepository.save(new Product(null, "Smartphone", "Latest model", 699.99, 10, null, electronics));
                productRepository.save(new Product(null, "T-Shirt", "Cotton T-shirt", 19.99, 50, null, clothing));
            }
            if (userRepository.count() == 0) {
                User user = new User();
                user.setEmail("admin@example.com");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole(Role.ADMIN);
                userRepository.save(user);
            }
        };
    }
}