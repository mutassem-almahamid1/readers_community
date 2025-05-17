package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.UpdateCommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
   CommentResponse createCommentOnReview(CommentRequest request, String userId);

//   CommentResponse createCommentOnPost(CommentRequest request, String userId);

   CommentResponse getCommentById(String id);

   List<CommentResponse> getByReviewId(String reviewId);

   Page<CommentResponse> getByReviewIdPaged(String reviewId, int page, int size);

//   List<CommentResponse> getCommentsByPostId(String postId);

//   Page<CommentResponse> getCommentsByPostIdPaged(String postId, int page, int size);

   List<CommentResponse> getByUserId(String userId);

   MessageResponse update(String id, UpdateCommentRequest request, String userId);

   MessageResponse softDeleteById(String id, String userId);

   MessageResponse hardDeleteById(String id, String userId);

   MessageResponse likeComment(String id, String userId);
}