package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.document.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String categoryId;
    private String categoryName;
    private String coverImage;
    private int reviewCount;
    private double avgRating;
    private String addedById;
    private String addedByUsername;
    private Status status;
    private LocalDateTime createdAt;
}
