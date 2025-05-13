package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    @Autowired
    private UserRepo userRepo;

    public Notification mapToDocument(NotificationRequest request) {
        return Notification.builder()
                .recipient(request.getRecipientId())
                .triggerUser(request.getTriggerUserId())
                .type(request.getType())
                .message(request.getMessage().trim())
                .review(request.getReviewId())
                .comment(request.getCommentId())
                .book(request.getBookId())
                .post(request.getPostId())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationResponse mapToResponse(Notification notification) {

        String recipientUsername = null;
        if (notification.getRecipient() != null) {
            User recipient = userRepo.getById(notification.getRecipient());
            if (recipient != null) {
                recipientUsername = recipient.getUsername();
            }
        }


        String triggerUsername = null;
        String triggerUserProfilePicture = null;
        if (notification.getTriggerUser() != null) {
            User triggerUser = userRepo.getById(notification.getTriggerUser());
            if (triggerUser != null) {
                triggerUsername = triggerUser.getUsername();
                triggerUserProfilePicture = triggerUser.getProfilePicture();
            }
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient())
                .recipientUsername(recipientUsername)
                .triggerUserId(notification.getTriggerUser())
                .triggerUsername(triggerUsername)
                .triggerUserProfilePicture(triggerUserProfilePicture)
                .type(notification.getType())
                .message(notification.getMessage())
                .reviewId(notification.getReview())
                .commentId(notification.getComment())
                .bookId(notification.getBook())
                .postId(notification.getPost())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public void updateDocument(Notification notification, NotificationRequest request) {
        notification.setRecipient(request.getRecipientId());
        notification.setTriggerUser(request.getTriggerUserId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage().trim());
        notification.setReview(request.getReviewId());
        notification.setComment(request.getCommentId());
        notification.setBook(request.getBookId());
        notification.setPost(request.getPostId());
        notification.setUpdatedAt(LocalDateTime.now());
    }
}
