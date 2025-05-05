package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Notification;
import com.project.readers_community.repository.NotificationRepo;
import com.project.readers_community.repository.mongo.NotificationRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepoImpl implements NotificationRepo {
    @Autowired
    private NotificationRepoMongo notificationRepoMongo;

    @Override
    public Notification save(Notification notification) {
        return notificationRepoMongo.save(notification);
    }

    @Override
    public Optional<Notification> getById(String id) {
        return notificationRepoMongo.findById(id).or(() -> {;
            throw new NotFoundException("Notification not found");
        });
    }

    @Override
    public List<Notification> getByRecipientId(String recipientId) {
        return notificationRepoMongo.findByRecipientId(recipientId);
    }

    @Override
    public Page<Notification> getByRecipientId(String recipientId, PageRequest pageRequest) {
        return notificationRepoMongo.findByRecipientId(recipientId, pageRequest);
    }

    @Override
    public List<Notification> getUnreadByRecipientId(String recipientId) {
        return notificationRepoMongo.findByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    public void deleteById(String id) {
        if (!notificationRepoMongo.existsById(id)) {
            throw new NotFoundException("Notification not found");
        }
        notificationRepoMongo.deleteById(id);
    }
}