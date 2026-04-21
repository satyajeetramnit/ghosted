package com.ghosted.config;

import com.ghosted.entity.User;
import com.ghosted.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class DataInitializer {

    private static final UUID DEFAULT_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            String defaultEmail = "default@example.com";
            if (userRepository.findById(DEFAULT_USER_ID).isEmpty() && 
                userRepository.findByEmail(defaultEmail).isEmpty()) {
                User user = new User();
                user.setId(DEFAULT_USER_ID);
                user.setEmail(defaultEmail);
                user.setPasswordHash("no-password"); // Placeholder
                user.setFirstName("Default");
                user.setLastName("User");
                userRepository.save(user);
                System.out.println("Default user created with ID: " + DEFAULT_USER_ID);
            } else {
                System.out.println("Default user already exists.");
            }
        };
    }
}
