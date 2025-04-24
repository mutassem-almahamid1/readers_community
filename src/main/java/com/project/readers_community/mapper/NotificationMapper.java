package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Comment;
import com.project.readers_community.model.document.Notification;
import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public Notification mapToDocument(NotificationRequest request, User recipient, User triggerUser, Review review, Comment comment, Book book, Post post) {
        return Notification.builder()
                .recipient(recipient)
                .triggerUser(triggerUser)
                .type(request.getType())
                .message(request.getMessage().trim())
                .review(review)
                .comment(comment)
                .book(book)
                .post(post)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient() != null ? notification.getRecipient().getId() : null)
                .recipientUsername(notification.getRecipient() != null ? notification.getRecipient().getUsername() : null)
                .triggerUserId(notification.getTriggerUser() != null ? notification.getTriggerUser().getId() : null)
                .triggerUsername(notification.getTriggerUser() != null ? notification.getTriggerUser().getUsername() : null)
                .type(notification.getType())
                .message(notification.getMessage())
                .reviewId(notification.getReview() != null ? notification.getReview().getId() : null)
                .commentId(notification.getComment() != null ? notification.getComment().getId() : null)
                .bookId(notification.getBook() != null ? notification.getBook().getId() : null)
                .postId(notification.getPost() != null ? notification.getPost().getId() : null)
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public void updateDocument(Notification notification, NotificationRequest request, User recipient, User triggerUser, Review review, Comment comment, Book book, Post post) {
        notification.setRecipient(recipient);
        notification.setTriggerUser(triggerUser);
        notification.setType(request.getType());
        notification.setMessage(request.getMessage().trim());
        notification.setReview(review);
        notification.setComment(comment);
        notification.setBook(book);
        notification.setPost(post);
    }
}