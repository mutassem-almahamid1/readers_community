package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.dto.request.UpdateReviewRequest;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

//@Component
public class ReviewMapper {

    @Autowired
    private UserRepo userRepo;

    public static Review mapToDocument(ReviewRequest request, String userId) {
        return Review.builder()
                .user(userId)
                .book(request.getBookId())
                .content(request.getContent() != null ? request.getContent().trim() : null)
                .rating(request.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
//                .comments(new ArrayList<>())
                .likedBy(new HashSet<>())
                .likeCount(0)
                .build();
    }

    public static ReviewResponse mapToResponse(Review review) {
//        User user = userRepo.getById(review.getUser());

//        boolean likedByCurrentUser = review.getLikedBy() != null && currentUserId != null &&
//                review.getLikedBy().contains(currentUserId);

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser())
//                .username(user != null ? user.getUsername() : null)
                .bookId(review.getBook())
                .content(review.getContent())
                .rating(review.getRating())
                .likedBy(review.getLikedBy())
                .likeCount(review.getLikeCount())
//                .likedByCurrentUser(likedByCurrentUser)
//                .commentsId(review.getComments())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static void updateDocument(Review review, UpdateReviewRequest request) {
//        review.setBook(request.getBookId());
        review.setContent(request.getContent() != null ? request.getContent().trim() : null);
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
    }
}