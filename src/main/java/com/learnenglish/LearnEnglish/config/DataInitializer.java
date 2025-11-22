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
            // === TẠO ADMIN ===
            String adminEmail = "admin@gmail.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setFullName("Administrator");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123")); // Mật khẩu: admin123
                admin.setRole(User.Role.ADMIN);        // ← Role ADMIN
                admin.setStatus(User.Status.ACTIVE);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());

                userRepository.save(admin);
                System.out.println("ADMIN ACCOUNT CREATED:");
                System.out.println("   Email: " + adminEmail);
                System.out.println("   Password: admin123");
                System.out.println("   Role: ADMIN");
            } else {
                System.out.println("Admin account already exists: " + adminEmail);
            }

            // === TẠO USER THƯỜNG (nếu cần) ===
            String testEmail = "test1@example.com";
            if (userRepository.findByEmail(testEmail).isEmpty()) {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setEmail(testEmail);
                testUser.setPassword(passwordEncoder.encode("123456"));
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