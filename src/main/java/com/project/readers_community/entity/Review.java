package com.project.readers_community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private String id;
    private String userId;
    private String username;
    private String bookId; // Added bookId property to match repository method
    private int rating;
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();

}
