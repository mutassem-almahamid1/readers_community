package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public Review mapToDocument(ReviewRequest request, User user, Book book) {
        return Review.builder()
                .user(user)
                .book(book)
                .content(request.getContent() != null ? request.getContent().trim() : null)
                .rating(request.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .likedBy(new HashSet<>())
                .build();
    }

    public ReviewResponse mapToResponse(Review review, String userId) {
        boolean likedByCurrentUser = review.getLikedBy() != null && userId != null &&
                review.getLikedBy().stream().anyMatch(user -> user.getId().equals(userId));


        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .username(review.getUser() != null ? review.getUser().getUsername() : null)
                .bookId(review.getBook() != null ? review.getBook().getId() : null)
                .createdAt(review.getCreatedAt())
                .likeCount(review.getLikedBy() != null ? review.getLikedBy().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .comments(review.getComments() != null ? review.getComments().stream()
                        .map(comment -> CommentResponse.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                                .createdAt(comment.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public void updateDocument(Review review, ReviewRequest request, Book book) {
        review.setBook(book);
        review.setContent(request.getContent() != null ? request.getContent().trim() : null);
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
    }
}