package com.learnenglish.LearnEnglish.config;

import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String testEmail = "test1@example.com";

            // Kiểm tra nếu user chưa tồn tại
            if (userRepository.findByEmail(testEmail).isEmpty()) {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setEmail(testEmail);
                testUser.setPassword(passwordEncoder.encode("123456")); // hash bằng BCrypt
                testUser.setRole(User.Role.USER);
                testUser.setStatus(User.Status.ACTIVE);
                testUser.setCreatedAt(LocalDateTime.now());
                testUser.setUpdatedAt(LocalDateTime.now());
                
                userRepository.save(testUser);
                System.out.println("Test user created: " + testEmail + " / 123456");
            } else {
                System.out.println("Test user already exists: " + testEmail);
            }
        };
    }
}
