package com.project.readers_community.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;

    @DBRef
    private User recipient; // المستخدم الذي سيتلقى الإشعار

    @DBRef
    private User triggerUser; // المستخدم الذي أثار الإشعار

    private NotificationType type; // نوع الإشعار

    private String message; // نص الإشعار

    @DBRef
    private Review review; // المراجعة المرتبطة (إن وجدت)

    @DBRef
    private Comment comment; // التعليق المرتبط (إن وجد)

    @DBRef
    private Book book; // الكتاب المرتبط (إن وجد)

    @DBRef
    private Post post; // المنشور المرتبط (إن وجد، مثل إعجاب بمنشور)

    private boolean isRead; // حالة الإشعار (مقروء أم غير مقروء)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}