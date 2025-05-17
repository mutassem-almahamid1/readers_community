package com.project.readers_community.model.document;

import com.project.readers_community.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    private String recipient;

    private String triggerUser;

    private NotificationType type;

    private String message;

    private String review;

    private String comment;

    private String book;

    private String post;

    private boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}