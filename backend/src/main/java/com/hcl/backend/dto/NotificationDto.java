package com.hcl.backend.dto;

import com.hcl.backend.Entity.Notification.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long userId;
}
