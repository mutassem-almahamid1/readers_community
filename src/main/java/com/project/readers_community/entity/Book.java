package com.project.readers_community.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
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
    private String category; // حقل واحد للتصنيف الرئيسي
    private String description;
    private List<String> categories = new ArrayList<>(); // قائمة التصنيفات
    private String publisher;
    private LocalDate publishedDate;
    private String coverImage;
    private String addedBy; // معرف المستخدم الذي أضاف الكتاب
    private List<Review> reviews = new ArrayList<>(); // قائمة المراجعات
    private int reviewCount; // عدد المراجعات
    private double avgRating; // متوسط التقييم
    private LocalDateTime createdAt = LocalDateTime.now();
    private String isbn; // ISBN identifier for the book

}
