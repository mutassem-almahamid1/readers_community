package com.project.readers_community.mapper;

import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.UpdateCommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;

public class CommentMapper {

    public static Comment mapToDocument(CommentRequest request, String userId) {
        return Comment.builder()
                .user(AssistantHelper.trimString(userId))
                .review(AssistantHelper.trimString(request.getReviewId()))
                .content(AssistantHelper.trimString(request.getContent()))
                .likedBy(new HashSet<>())
                .likeCount(0)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser())
                .reviewId(comment.getReview())
                .content(comment.getContent())
                .likedByUserIds(comment.getLikedBy())
                .likeCount(comment.getLikeCount())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponse mapToResponse(Comment comment, Map<String, UserResponse> userResponseMap) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser())
                .reviewId(comment.getReview())
                .content(comment.getContent())
                .likedByUserIds(comment.getLikedBy())
                .likeCount(comment.getLikeCount())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static void updateDocument(Comment comment, UpdateCommentRequest request) {
        comment.setContent(request.getContent().trim());
        comment.setUpdatedAt(LocalDateTime.now());
    }
}
