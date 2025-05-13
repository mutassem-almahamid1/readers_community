package com.project.readers_community.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    private String id;

    @DBRef
    private String user;

    @DBRef
    private String book;

    private String content;

    private int rating;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @Builder.Default
    private Set<String> likedBy = new HashSet<>(); // المستخدمون الذين أعجبوا بالمراجعة
    @Builder.Default
    private List<String> comments = new ArrayList<>(); // قائمة التعليقات على المراجعة
}




