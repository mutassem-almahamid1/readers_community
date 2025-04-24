package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    CommentResponse createCommentOnReview(CommentRequest request, String userId);
    CommentResponse createCommentOnPost(CommentRequest request, String userId);
    CommentResponse getCommentById(String id);
    List<CommentResponse> getByReviewId(String reviewId);
    Page<CommentResponse> getByReviewIdPaged(String reviewId, int page, int size);
    List<CommentResponse> getCommentsByPostId(String postId);
    Page<CommentResponse> getCommentsByPostIdPaged(String postId, int page, int size);
    List<CommentResponse> getByUserId(String userId);
    CommentResponse update(String id, CommentRequest request, String userId);
    CommentResponse softDeleteById(String id, String userId);
    MessageResponse hardDeleteById(String id, String userId);
    CommentResponse likeComment(String id, String userId);
}