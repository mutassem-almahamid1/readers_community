package com.project.readers_community.model.document;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    private String id;
    private String category;
    private String title;
    private String author;
    private String description;
    private String coverImage;

//    private List<String> reviews = new ArrayList<>();
    private int reviewCount = 0;
    private double avgRating = 0.0;
    private int readerCount ;
    private String addedBy;

    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}

