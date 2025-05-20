package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{recipientId}/all")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable String recipientId, @RequestParam(required = false) NotificationType type) {
        List<NotificationResponse> notifications = notificationService.getByRecipientId(recipientId, type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{recipientId}/paged")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsPaged(
            @PathVariable String recipientId,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = notificationService.getByRecipientIdPaged(recipientId, type, page, size);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationsById(@PathVariable String id) {
        NotificationResponse notifications = notificationService.getById(id);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable String id) {
        MessageResponse updatedNotification = notificationService.markAsRead(id);
        return ResponseEntity.ok(updatedNotification);
    }

    @PatchMapping("/{recipientId}/read-all")
    public ResponseEntity<MessageResponse> markAsReadByRecipientId(@PathVariable String recipientId) {
        MessageResponse updatedNotification = notificationService.markAsReadByRecipientId(recipientId);
        return ResponseEntity.ok(updatedNotification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> hardDeleteNotification(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.hardDeleteById(id));
    }
}