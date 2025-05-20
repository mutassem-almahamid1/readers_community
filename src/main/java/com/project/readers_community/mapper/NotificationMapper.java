package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.*;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

public class NotificationMapper {
    public static Notification mapToDocument(NotificationRequest request) {
        return Notification.builder()
                .recipient(request.getRecipientId())
                .triggerUser(request.getTriggerUserId())
                .type(request.getType())
                .message(request.getMessage().trim())
                .review(request.getReviewId())
                .comment(request.getCommentId())
                .book(request.getBookId())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NotificationResponse mapToResponse(
            Notification notification, Map<String, UserResponse> userResponseMap,
            Map<String, BookResponse> bookResponseMap, Map<String, ReviewResponse> reviewResponseMap,
            Map<String, CommentResponse> commentResponseMap
    ) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient())
                .triggerUserId(notification.getTriggerUser())
                .triggerUser(userResponseMap.getOrDefault(notification.getTriggerUser(), null))
                .type(notification.getType())
                .message(notification.getMessage())
                .reviewId(notification.getReview())
                .review(notification.getReview() != null ? reviewResponseMap.getOrDefault(notification.getReview(), null) : null)
                .commentId(notification.getComment())
                .comment(notification.getComment() != null ? commentResponseMap.getOrDefault(notification.getComment(), null) : null)
                .bookId(notification.getBook())
                .book(notification.getBook() != null ? bookResponseMap.getOrDefault(notification.getBook(), null) : null)
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static void updateDocument(Notification notification, NotificationRequest request) {
        notification.setRecipient(request.getRecipientId());
        notification.setTriggerUser(request.getTriggerUserId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage().trim());
        notification.setReview(request.getReviewId());
        notification.setComment(request.getCommentId());
        notification.setBook(request.getBookId());
        notification.setUpdatedAt(LocalDateTime.now());
    }
}
