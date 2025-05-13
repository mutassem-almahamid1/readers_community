package com.project.readers_community.service.impl;

import com.project.readers_community.model.dto.response.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, NotificationResponse notification) {
        messagingTemplate.convertAndSendToUser(
                userId,
                "/topic/notifications",
                notification
        );
    }
    
    public void sendNotificationCount(String userId, long count) {
        messagingTemplate.convertAndSendToUser(
                userId,
                "/topic/notification-count",
                count
        );
    }
}