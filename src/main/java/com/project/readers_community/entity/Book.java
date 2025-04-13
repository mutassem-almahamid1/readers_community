package com.project.readers_community.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


@Document(collection = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private String categoryId;
    private String description;
    private LocalDate publishedDate;
    private String coverImage;
    private String addByUserId; // معرف المستخدم الذي أضاف الكتاب
    private int reviewCount; // عدد المراجعات
    private double avgRating; // متوسط التقييم
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}