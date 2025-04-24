package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.NotificationType;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    NotificationResponse create(NotificationRequest request);
    NotificationResponse getById(String id);
    List<NotificationResponse> getByRecipientId(String recipientId);
    Page<NotificationResponse> getByRecipientIdPaged(String recipientId, int page, int size);
    List<NotificationResponse> getUnreadByRecipientId(String recipientId);
    NotificationResponse update(String id, NotificationRequest request);
    NotificationResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    NotificationResponse markAsRead(String id);

    void createNotificationAsync(String recipientId, String triggerUserId,
                                 NotificationType type, String message,
                                 String reviewId, String commentId, String bookId, String postId);


}