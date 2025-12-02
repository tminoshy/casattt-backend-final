package com.bookmanagement.controller;

import com.bookmanagement.dto.NotificationResponse;
import com.bookmanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotification(@PathVariable String userId) {
        List<NotificationResponse> response = notificationService.getAllNotification(userId);

        return ResponseEntity.ok(response);
    }

}
