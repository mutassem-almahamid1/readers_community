package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.PostResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest request, String userId);
    PostResponse getById(String id);
    List<PostResponse> getAll();
    Page<PostResponse> getAllPage(int page, int size);
    List<PostResponse> getByUserId(String userId);

    Page<PostResponse> getAllPageByUser(String userId, int page, int size);

    Page<PostResponse> getAllPageByReview(String reviewId, int page, int size);

    List<PostResponse> getByReviewId(String reviewId);
    PostResponse likePost(String id, String userId);
    PostResponse update(String id, PostRequest request, String userId);
    PostResponse softDeleteById(String id, String userId);
    MessageResponse hardDeleteById(String id, String userId);
}