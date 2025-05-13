package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.ForbiddenException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
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
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Review review = reviewRepo.getById(request.getReviewId());
        if (review == null) {
            throw new NotFoundException("Review not found");
        }


        if (!review.getUser().equals(userId)) {
            throw new ForbiddenException("You can only create a post for your own review");
        }

        Post post = postMapper.mapToDocument(request, userId, review.getId());
        Post savedPost = postRepo.save(post);

        return postMapper.mapToResponse(savedPost, userId);
    }

    @Override
    public PostResponse getById(String id) {
        Post post = postRepo.getById(id);
        if (post == null || post.getStatus() != Status.ACTIVE) {
            throw new NotFoundException("Post not found");
        }
        return postMapper.mapToResponse(post, null);
    }

    @Override
    public List<PostResponse> getAll() {
        List<Post> posts = postRepo.getAll();
        return posts.stream()
                .filter(post -> post.getStatus() == Status.ACTIVE)
                .map(post -> postMapper.mapToResponse(post, null))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepo.getAllPage(pageRequest);
        return posts.map(post -> {
            if (post.getStatus() == Status.ACTIVE) {
                return postMapper.mapToResponse(post, null);
            }
            return null;
        });
    }

    @Override
    public Page<PostResponse> getAllPageByUser(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepo.getAllPageByUser(userId, pageRequest);
        return posts.map(post -> {
            if (post.getStatus() == Status.ACTIVE) {
                return postMapper.mapToResponse(post, null);
            }
            return null;
        });
    }

    @Override
    public Page<PostResponse> getAllPageByReview(String reviewId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepo.getAllPageByReview(reviewId, pageRequest);
        return posts.map(post -> {
            if (post.getStatus() == Status.ACTIVE) {
                return postMapper.mapToResponse(post, null);
            }
            return null;
        });
    }

    @Override
    public List<PostResponse> getByReviewId(String reviewId) {
        List<Post> posts = postRepo.findByReviewId(reviewId);
        return posts.stream()
                .filter(post -> post.getStatus() == Status.ACTIVE)
                .map(post -> postMapper.mapToResponse(post, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getByUserId(String userId) {
        List<Post> posts = postRepo.findByUserId(userId);
        return posts.stream()
                .filter(post -> post.getStatus() == Status.ACTIVE)
                .map(post -> postMapper.mapToResponse(post, userId))
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse likePost(String id, String userId) {
        Post post = postRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getStatus() != Status.ACTIVE) {
            throw new NotFoundException("Post not found");
        }
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }


        post.getLikedBy().add(userId);
        Post updatedPost = postRepo.save(post);

        return postMapper.mapToResponse(updatedPost, userId);
    }

    @Override
    public PostResponse update(String id, PostRequest request, String userId) {
        Post post = postRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));


        if (!post.getUser().equals(userId)) {
            throw new ForbiddenException("You can only update your own posts");
        }

        Review newReview = reviewRepo.getById(request.getReviewId());
        if (newReview == null) {
            throw new NotFoundException("Review not found");
        }


        if (!newReview.getUser().equals(userId)) {
            throw new ForbiddenException("You can only link a post to your own review");
        }

        postMapper.updateDocument(post, newReview.getId());
        Post updatedPost = postRepo.save(post);

        return postMapper.mapToResponse(updatedPost, userId);
    }

    @Override
    public PostResponse softDeleteById(String id, String userId) {
        Post post = postRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getStatus() != Status.ACTIVE) {
            throw new NotFoundException("Post not found");
        }
        if (!post.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own posts");
        }

        post.setStatus(Status.DELETED);
        post.setDeletedAt(LocalDateTime.now());

        Post updatedPost = postRepo.save(post);

        return postMapper.mapToResponse(updatedPost, userId);
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Post post = postRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getStatus() != Status.ACTIVE) {
            throw new NotFoundException("Post not found");
        }
        if (!post.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own posts");
        }

        postRepo.deleteById(id);

        return MessageResponse.builder().message("Post deleted successfully").build();
    }
}
