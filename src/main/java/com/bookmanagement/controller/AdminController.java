package com.bookmanagement.controller;

import com.bookmanagement.dto.AddAdminRequest;
import com.bookmanagement.dto.AdminResponse;
import com.bookmanagement.dto.UpdateProfileRequest;
import com.bookmanagement.entity.User;
import com.bookmanagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody AddAdminRequest request) {
        try {
            User admin = adminService.addAdmin(request);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        try {
            adminService.deleteAdmin(adminId);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/profile/{adminId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long adminId, @RequestBody UpdateProfileRequest request) {
        try {
            User admin = adminService.updateProfile(adminId, request);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

