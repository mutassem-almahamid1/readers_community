package com.project.readers_community.service.impl;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.PostResponse;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.mapper.PostMapper;
import com.project.readers_community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostMapper postMapper;

    @Override
    public PostResponse create(PostRequest request, String userId) {

        Review review = reviewRepo.getById(request.getReviewId());


        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only create a post from your own review");
        }


        Post post = postMapper.mapToDocument(request, review);
        Post savedPost = postRepo.save(post);

        return postMapper.mapToResponse(savedPost);
    }

    @Override
    public PostResponse getById(String id) {
        Post post = postRepo.getById(id);
        return postMapper.mapToResponse(post);
    }

    @Override
    public List<PostResponse> getAll() {
        List<Post> posts = postRepo.getAll();
        return posts.stream()
                .map(postMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepo.getAllPage(pageRequest);
        return posts.map(postMapper::mapToResponse);
    }

    @Override
    public List<PostResponse> getByUserId(String userId) {
        List<Post> posts = postRepo.findByUserId(userId);
        return posts.stream()
                .map(postMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getByReviewId(String reviewId) {
        List<Post> posts = postRepo.findByReviewId(reviewId);
        return posts.stream()
                .map(postMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse update(String id, PostRequest request, String userId) {

        Post post = postRepo.getById(id);


        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own posts");
        }


        Review review = reviewRepo.getById(request.getReviewId());


        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update a post with your own review");
        }


        String content = review.getContent() != null
                ? review.getContent().trim()
                : "Rated the book with " + review.getRating() + " stars";
        post.setReview(review);
        post.setContent(content);
        post.setRating(review.getRating());
        post.setUpdatedAt(LocalDateTime.now());


        Post updatedPost = postRepo.save(post);

        return postMapper.mapToResponse(updatedPost);
    }

    @Override
    public PostResponse softDeleteById(String id, String userId) {

        Post post = postRepo.getById(id);


        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own posts");
        }


        post.setStatus(Status.DELETED);
        post.setDeletedAt(LocalDateTime.now());

        Post updatedPost = postRepo.save(post);

        return postMapper.mapToResponse(updatedPost);
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {

        Post post = postRepo.getById(id);

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own posts");
        }


        postRepo.deleteById(id);

        return MessageResponse.builder().message("Post deleted successfully").build();
    }
}