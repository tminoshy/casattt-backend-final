package com.bookmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn
    private Book book;

    @ManyToOne
    @JoinColumn
    private Borrowing borowing;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status", nullable = false)
    private NotificationStatus notiticationStatus;

    @Column(name = "issue_at", nullable = false)
    private LocalDateTime issueAt;

    @Column(name = "message", nullable = false)
    private String message;

    public enum NotificationStatus {
        SUCCESSFULLY_BORROWED,
        SUCCESSFULLY_RETURNED,
        WARNING
    }
}
