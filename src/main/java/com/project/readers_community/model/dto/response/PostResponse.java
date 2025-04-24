package com.project.readers_community.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private String reviewId;
    private String userName;
    private String userProfilePicture;
    private String bookId;
    private String bookTitle;
    private String bookCoverImage;
    private String authorName;
    private String content;
    private int rating;
    private int likeCount;
    private boolean likedByCurrentUser;
    private List<CommentResponse> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}