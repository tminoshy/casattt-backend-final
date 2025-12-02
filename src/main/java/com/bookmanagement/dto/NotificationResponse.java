package com.bookmanagement.dto;

import com.bookmanagement.entity.Notification;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Notification.NotificationStatus notificationStatus;
    private String message;
}
