package com.bookmanagement.service;

import com.bookmanagement.dto.NotificationResponse;
import com.bookmanagement.entity.Borrowing;
import com.bookmanagement.entity.Notification;
import com.bookmanagement.entity.User;
import com.bookmanagement.repository.BorrowingRepository;
import com.bookmanagement.repository.NotificationRepository;
import com.bookmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final BorrowingRepository borrowingRepository;

    private final UserRepository userRepository;

    public List<NotificationResponse> getAllNotification(String userId) {

        Long uid = Long.parseLong(userId);

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate deadline = LocalDate.now().minusDays(7);

        System.out.println(deadline);

        // 1. Find overdue borrowings
        List<Borrowing> overdueList = borrowingRepository.findOverdueBorrowings(uid, deadline);

        for (Borrowing b : overdueList) {
            System.out.println("bokkkkkkkkkkkkkkkkkkkkkkkkkkkk");

            String msg = "Alert: You should return the book '"
                    + b.getBook().getBookName()
                    + "' — it has been more than 7 days.";

            boolean alreadyExists = notificationRepository.existsWarning(uid, msg);

            if (!alreadyExists) {
                Notification n = new Notification();
                n.setUser(user);
                n.setBook(b.getBook());
                n.setBorowing(b);
                n.setIssueAt(LocalDateTime.now());
                n.setMessage(msg);
                n.setNotiticationStatus(Notification.NotificationStatus.WARNING);

                notificationRepository.save(n);
            }
        }

        // 2. Fetch all notifications
        List<Notification> notifications = notificationRepository.findByUserId(uid);

        return notifications.stream()
                .map(n -> NotificationResponse.builder()
                        .message(n.getMessage())
                        .notificationStatus(n.getNotiticationStatus())
                        .build())
                .collect(Collectors.toList());
    }

}
