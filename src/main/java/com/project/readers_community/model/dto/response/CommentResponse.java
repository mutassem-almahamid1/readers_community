package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String id;
    private String userId;
    private UserResponse user;
    private String reviewId;
    private String content;
    private Set<String> likedByUserIds;
    private int likeCount;
    private Status status;
    private LocalDateTime createdAt;
}