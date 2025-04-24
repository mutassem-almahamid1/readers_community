package com.project.readers_community.mapper;

import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public Comment mapToDocument(CommentRequest request, User user, Review review, Post post) {
        return Comment.builder()
                .user(user)
                .review(review)
                .post(post)
                .content(request.getContent().trim())
                .likedBy(new HashSet<>())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public CommentResponse mapToResponse(Comment comment , String currentUserId) {
        boolean likedByCurrentUser = comment.getLikedBy().stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .userProfilePicture(comment.getUser().getProfilePicture())
                .reviewId(comment.getReview() != null ? comment.getReview().getId() : null)
                .postId(comment.getPost()!= null? comment.getPost().getId() : null)
                .content(comment.getContent())
                .likedByUserIds(comment.getLikedBy() != null
                        ? comment.getLikedBy().stream().map(User::getId).collect(Collectors.toSet())
                        : new HashSet<>())
                .likedByCurrentUser(likedByCurrentUser)
                .likeCount(comment.getLikedBy().size())
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