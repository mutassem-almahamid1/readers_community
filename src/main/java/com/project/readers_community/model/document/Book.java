package com.project.readers_community.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    private String id;
    @Indexed(unique = true)
    private String title;
    private String author;
    private String description;
    @DBRef
    private Category category;
    private String coverImage;
    @DBRef
    private List<Review> reviews = new ArrayList<>();
    private int reviewCount = 0;
    private double avgRating = 0.0;
    @DBRef
    private User addedBy;
    private Status status=Status.ACTIVE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int readerCount ;
}

