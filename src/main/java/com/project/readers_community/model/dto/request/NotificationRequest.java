package com.project.readers_community.model.dto.request;

import com.project.readers_community.model.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    @NotBlank(message = "Recipient ID is required")
    private String recipientId;

    @NotBlank(message = "Trigger user ID is required")
    private String triggerUserId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Message is required")
    @Size(max = 250, message = "Message must be 200 characters or less")
    private String message;

    private String reviewId;
    private String commentId;
    private String bookId;
    private String postId;
}