package com.bookmanagement.config;

import com.bookmanagement.entity.User;
import com.bookmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin3");
            admin.setPassword("secret");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@bookmanagement.com");
            admin.setPhoneNumber("0000000000");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin created: username=admin, password=secret");
        }
    }
}

