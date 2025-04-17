package com.project.readers_community.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String recipientId; // معرف المستخدم المستلم
    private String type; // نوع الإشعار (مثل: إعجاب، تعليق)
    private String content; // محتوى الإشعار
    private boolean read; // هل تم قراءته أم لا
    private LocalDateTime createdAt = LocalDateTime.now();
}