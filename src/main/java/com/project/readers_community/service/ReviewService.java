package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.request.UpdateReviewRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(ReviewRequest request, String userId);

    ReviewResponse getById(String id);

    List<ReviewResponse> getAll();

    Page<ReviewResponse> getAllPage(int page, int size);

    List<ReviewResponse> getByBookId(String bookId);

    Page<ReviewResponse> getByBookIdPage(String bookId, int page, int size);

    List<ReviewResponse> getByUserId(String userId);

    Page<ReviewResponse> getByUserIdPage(String userId, int page, int size);

    ReviewResponse likeReview(String id, String userId);

    MessageResponse update(String id, UpdateReviewRequest request, String userId);

    MessageResponse softDeleteById(String id, String userId);

    MessageResponse hardDeleteById(String id, String userId);
}