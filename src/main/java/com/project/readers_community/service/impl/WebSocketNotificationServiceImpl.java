package com.project.readers_community.service.impl;


import com.project.readers_community.config.CustomWebSocketHandler;
import com.project.readers_community.service.WebSocketNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {

    @Autowired
    private CustomWebSocketHandler customWebSocketHandler;

    @Override
    public void notifyNewNotification(Object notification, String userId) {
        try {
            customWebSocketHandler.sendNotification(notification, userId);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}
