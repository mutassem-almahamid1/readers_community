package com.project.readers_community.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.put(userId, session);
            log.info("New WebSocket connection established: {}", userId);
        } else {
            session.close(CloseStatus.BAD_DATA);
            log.info("WebSocket connection closed due to missing userId.");
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages if needed
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("WebSocket connection closed: {}", userId);
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        String uri = session.getUri().toString();
        String[] parts = uri.split("userId=");
        String userId = (parts.length > 1) ? parts[1] : null;
        log.info("Extracted storeId: {}", userId);
        return userId;
    }

    public void sendNotification(Object notification, String userId) throws Exception {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            String jsonResponse = objectMapper.writeValueAsString(notification);
            session.sendMessage(new TextMessage(jsonResponse));
            log.info("Notification sent: {}", jsonResponse);
        } else {
            log.info("No active WebSocket session for user ID: {}", userId);
        }
    }
}





