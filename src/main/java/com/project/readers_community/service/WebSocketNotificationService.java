package com.project.readers_community.service;


public interface WebSocketNotificationService {

    void notifyNewNotification(Object notification, String userId);
}
