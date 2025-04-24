package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.document.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String recipientId;
    private String recipientUsername;
    private String triggerUserId;
    private String triggerUsername;
    private NotificationType type;
    private String message;
    private String reviewId;
    private String commentId;
    private String bookId;
    private String postId;
    private boolean isRead;
    private LocalDateTime createdAt;
}