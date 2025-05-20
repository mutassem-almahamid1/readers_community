package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.enums.NotificationType;
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
    private String triggerUserId;
    private UserResponse triggerUser;
    private NotificationType type;
    private String message;
    private String reviewId;
    private ReviewResponse review;
    private String commentId;
    private CommentResponse comment;
    private String bookId;
    private BookResponse book;
    private boolean isRead;
    private LocalDateTime createdAt;
}