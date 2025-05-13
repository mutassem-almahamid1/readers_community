package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    @Autowired
    private UserRepo userRepo;

    public Review mapToDocument(ReviewRequest request, String userId) {
        return Review.builder()
                .user(userId)
                .book(request.getBookId())
                .content(request.getContent() != null ? request.getContent().trim() : null)
                .rating(request.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .likedBy(new HashSet<>())
                .build();
    }

    public ReviewResponse mapToResponse(Review review, String currentUserId) {
        User user = userRepo.getById(review.getUser());

        boolean likedByCurrentUser = review.getLikedBy() != null && currentUserId != null &&
                review.getLikedBy().contains(currentUserId);

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser())
                .username(user != null ? user.getUsername() : null)
                .bookId(review.getBook())
                .content(review.getContent())
                .rating(review.getRating())
                .likeCount(review.getLikedBy() != null ? review.getLikedBy().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .commentsId(review.getComments())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public void updateDocument(Review review, ReviewRequest request) {
        review.setBook(request.getBookId());
        review.setContent(request.getContent() != null ? request.getContent().trim() : null);
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
    }
}