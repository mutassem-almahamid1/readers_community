package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.repository.NotificationRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.mapper.NotificationMapper;
import com.project.readers_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NotificationMapper notificationMapper;

    @Async
    @Override
    public void createNotificationAsync(String recipientId, String triggerUserId,
                                        NotificationType type, String message,
                                        String reviewId, String commentId, String bookId, String postId) {
        // Check
        if (userRepo.getById(recipientId) == null || userRepo.getById(triggerUserId) == null) {
            return;
        }


        NotificationRequest request = new NotificationRequest();
        request.setRecipientId(recipientId);
        request.setTriggerUserId(triggerUserId);
        request.setType(type);
        request.setMessage(message);
        request.setReviewId(reviewId);
        request.setCommentId(commentId);
        request.setBookId(bookId);
        request.setPostId(postId);

        // Create the notification
        create(request);
    }

    @Override
    public NotificationResponse create(NotificationRequest request) {
        // Validate users exist
        if (userRepo.getById(request.getRecipientId()) == null) {
            throw new NotFoundException("Recipient user not found");
        }
        if (userRepo.getById(request.getTriggerUserId()) == null) {
            throw new NotFoundException("Trigger user not found");
        }

        Notification notification = notificationMapper.mapToDocument(request);
        Notification savedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(savedNotification);
    }

    @Override
    public NotificationResponse getById(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        return notificationMapper.mapToResponse(notification);
    }

    @Override
    public List<NotificationResponse> getByRecipientId(String recipientId) {
        List<Notification> notifications = notificationRepo.getByRecipientId(recipientId);
        return notifications.stream()
                .map(notificationMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponse> getByRecipientIdPaged(String recipientId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepo.getByRecipientId(recipientId, pageRequest);
        return notifications.map(notificationMapper::mapToResponse);
    }

    @Override
    public List<NotificationResponse> getUnreadByRecipientId(String recipientId) {
        List<Notification> notifications = notificationRepo.getUnreadByRecipientId(recipientId);
        return notifications.stream()
                .map(notificationMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponse update(String id, NotificationRequest request) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        
        // Validate users exist
        if (userRepo.getById(request.getRecipientId()) == null) {
            throw new NotFoundException("Recipient user not found");
        }
        if (userRepo.getById(request.getTriggerUserId()) == null) {
            throw new NotFoundException("Trigger user not found");
        }
                
        notificationMapper.updateDocument(notification, request);
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }

    @Override
    public NotificationResponse softDeleteById(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        notificationRepo.deleteById(id);
        return MessageResponse.builder().message("Notification deleted successfully").build();
    }

    @Override
    public NotificationResponse markAsRead(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }
}
