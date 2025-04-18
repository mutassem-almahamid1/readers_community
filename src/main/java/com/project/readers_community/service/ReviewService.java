package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(ReviewRequest request, String userId);
    ReviewResponse getById(String id);
    List<ReviewResponse> getAll();
    Page<ReviewResponse> getAllPage(int page, int size);
    List<ReviewResponse> getByBookId(String bookId);

    List<ReviewResponse> getByUserId(String userId);

    ReviewResponse update(String id, ReviewRequest request, String userId);
    ReviewResponse softDeleteById(String id, String userId);
    MessageResponse hardDeleteById(String id, String userId);
}