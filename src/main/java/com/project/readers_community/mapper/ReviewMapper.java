package com.project.readers_community.mapper;

import com.project.readers_community.mapper.helper.AssistantHelper;
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


    public static Review mapToDocument(ReviewRequest request, String userId) {
        return Review.builder()
                .user(AssistantHelper.trimString(userId))
                .book(AssistantHelper.trimString(request.getBookId()))
                .content(AssistantHelper.trimString(request.getContent()))
                .rating(request.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .likedBy(new HashSet<>())
                .likeCount(0)
                .build();
    }

    public static ReviewResponse mapToResponse(Review review) {

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser())
                .bookId(review.getBook())
                .content(review.getContent())
                .rating(review.getRating())
                .likedBy(review.getLikedBy())
                .likeCount(review.getLikeCount())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static void updateDocument(Review review, UpdateReviewRequest request) {
        review.setContent(AssistantHelper.trimString(request.getContent()));
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
    }
}