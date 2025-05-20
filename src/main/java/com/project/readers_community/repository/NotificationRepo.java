package com.project.readers_community.repository;

import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface NotificationRepo {
    Notification save(Notification notification);

    List<Notification> saveAll(List<Notification> notifications);

    Optional<Notification> getById(String id);

    List<Notification> getByRecipientId(String recipientId);

    List<Notification> getByRecipientIdAndType(String recipientId, NotificationType type);

    Page<Notification> getByRecipientId(String recipientId, PageRequest pageRequest);

    Page<Notification> getByRecipientIdAndType(String recipientId, NotificationType type, PageRequest pageRequest);

    List<Notification> getUnreadByRecipientId(String recipientId);

    void deleteById(String id);
}