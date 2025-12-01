package com.bookmanagement.service;

import com.bookmanagement.dto.AddAdminRequest;
import com.bookmanagement.dto.AdminResponse;
import com.bookmanagement.dto.UpdateProfileRequest;
import com.bookmanagement.entity.User;
import com.bookmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addAdmin(AddAdminRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User admin = new User();
        // Split name into firstName and lastName
        String[] nameParts = request.getName().split(" ", 2);
        admin.setFirstName(nameParts[0]);
        admin.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setRole(User.Role.ADMIN);

        return userRepository.save(admin);
    }

    public void deleteAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }

        userRepository.delete(admin);
    }

    public List<AdminResponse> getAllAdmins() {
        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
        return admins.stream().map(admin -> {
            AdminResponse response = new AdminResponse();
            response.setName(admin.getFirstName() + " " + admin.getLastName());
            response.setUsername(admin.getUsername());
            response.setEmail(admin.getEmail());
            response.setPhoneNumber(admin.getPhoneNumber());
            return response;
        }).collect(Collectors.toList());
    }

    public User updateProfile(Long adminId, UpdateProfileRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }

        // Split name into firstName and lastName
        if (request.getName() != null) {
            String[] nameParts = request.getName().split(" ", 2);
            admin.setFirstName(nameParts[0]);
            admin.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        }

        if (request.getUsername() != null) {
            if (!admin.getUsername().equals(request.getUsername()) && 
                userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            admin.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            if (!admin.getEmail().equals(request.getEmail()) && 
                userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            admin.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPhoneNumber() != null) {
            admin.setPhoneNumber(request.getPhoneNumber());
        }

        return userRepository.save(admin);
    }
}

