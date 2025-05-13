package com.project.readers_community.controller;

import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{recipientId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable String recipientId) {
        List<NotificationResponse> notifications = notificationService.getByRecipientId(recipientId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{recipientId}/paged")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsPaged(
            @PathVariable String recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = notificationService.getByRecipientIdPaged(recipientId, page, size);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{recipientId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@PathVariable String recipientId) {
        List<NotificationResponse> unreadNotifications = notificationService.getUnreadByRecipientId(recipientId);
        return ResponseEntity.ok(unreadNotifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable String id) {
        NotificationResponse updatedNotification = notificationService.markAsRead(id);
        return ResponseEntity.ok(updatedNotification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> hardDeleteNotification(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.hardDeleteById(id));
    }
}