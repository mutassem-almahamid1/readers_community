package com.project.readers_community.mapper;

import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class CommentMapper {

    @Autowired
    private UserRepo userRepo;

    public Comment mapToDocument(CommentRequest request, String userId) {
        return Comment.builder()
                .user(userId)
                .review(request.getReviewId() != null ? request.getReviewId().trim() : null)
                .post(request.getPostId() != null ? request.getPostId().trim() : null)
                .content(request.getContent().trim())
                .likedBy(new HashSet<>())
                .likeCount(0)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public CommentResponse mapToResponse(Comment comment, String currentUserId) {
        User user = userRepo.getById(comment.getUser());
        boolean likedByCurrentUser = comment.getLikedBy() != null && 
                currentUserId != null &&
                comment.getLikedBy().contains(currentUserId);

        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser())
                .username(user != null ? user.getUsername() : null)
                .userProfilePicture(user != null ? user.getProfilePicture() : null)
                .reviewId(comment.getReview())
                .postId(comment.getPost())
                .content(comment.getContent())
                .likedByUserIds(comment.getLikedBy())
                .likedByCurrentUser(likedByCurrentUser)
                .likeCount(comment.getLikeCount())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }

    public void updateDocument(Comment comment, CommentRequest request) {
        comment.setContent(request.getContent().trim());
        comment.setUpdatedAt(LocalDateTime.now());
    }
}
