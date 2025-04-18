package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .build();
    }

    public ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser() != null ? review.getUser().getId() : null)
                .username(review.getUser() != null ? review.getUser().getUsername() : null)
                .bookId(review.getBook() != null ? review.getBook().getId() : null)
                .bookTitle(review.getBook() != null ? review.getBook().getTitle() : null)
                .content(review.getContent())
                .rating(review.getRating())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public void updateDocument(Review review, ReviewRequest request, Book book) {
        review.setBook(book);
        review.setContent(request.getContent() != null ? request.getContent().trim() : null);
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
    }
}