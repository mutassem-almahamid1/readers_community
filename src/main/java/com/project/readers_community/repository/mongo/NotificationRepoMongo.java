package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepoMongo extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientAndIsRead(String recipient, boolean isRead);

    Page<Notification> findByRecipient(String recipient, PageRequest pageRequest);

    Page<Notification> findByRecipientAndType(String recipient, NotificationType type, PageRequest pageRequest);

    List<Notification> findByRecipient(String recipient);

    List<Notification> findByRecipientAndType(String recipient, NotificationType type);
}
