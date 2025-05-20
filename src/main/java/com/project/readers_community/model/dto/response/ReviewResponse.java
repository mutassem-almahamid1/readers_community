package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private String id;
    private String userId;
    private UserResponse user;
    private String bookId;
    private BookResponse book;
    private String content;
    private int rating;
    private Set<String> likedBy;
    private Set<UserResponse> likedByUsers;
    private int likeCount;
    private List<CommentResponse> comments;
    private Status status;
    private LocalDateTime createdAt;
}