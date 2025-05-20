package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    NotificationResponse create(NotificationRequest request);

    NotificationResponse getById(String id);

    List<NotificationResponse> getByRecipientId(String recipientId, NotificationType type);

    Page<NotificationResponse> getByRecipientIdPaged(String recipientId, NotificationType type, int page, int size);

    MessageResponse hardDeleteById(String id);

    MessageResponse markAsRead(String id);

    MessageResponse markAsReadByRecipientId(String recipientId);

//    void createNotificationAsync(String recipientId, String triggerUserId,
//                                 NotificationType type, String message,
//                                 String reviewId, String commentId, String bookId);


}