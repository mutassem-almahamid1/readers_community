package com.project.readers_community.repository;

import com.project.readers_community.model.document.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface NotificationRepo {
    Notification save(Notification notification);
    Optional<Notification> getById(String id);
    List<Notification> getByRecipientId(String recipientId);
    Page<Notification> getByRecipientId(String recipientId, PageRequest pageRequest);
    List<Notification> getUnreadByRecipientId(String recipientId);
    void deleteById(String id);
}