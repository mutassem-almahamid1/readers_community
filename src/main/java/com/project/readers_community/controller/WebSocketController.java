package com.project.readers_community.controller;

import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/notifications.getUnread")
    public List<NotificationResponse> getUnreadNotifications(SimpMessageHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            String userId = principal.getName();
            return notificationService.getUnreadByRecipientId(userId);
        }
        return List.of();
    }

    @MessageMapping("/notifications.markAsRead")
    public void markNotificationAsRead(@Payload String notificationId) {
        notificationService.markAsRead(notificationId);
    }
}