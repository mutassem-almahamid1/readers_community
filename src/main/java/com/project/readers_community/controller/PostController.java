package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.PostResponse;
import com.project.readers_community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request,
            @RequestParam String userId) {
        PostResponse response = postService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable String id) {
        PostResponse response = postService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> response = postService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PostResponse>> getAllPostsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> response = postService.getAllPage(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged/review/{reviewId}")
    public ResponseEntity<Page<PostResponse>> getAllPostsPagedByReviewId(
            @PathVariable String reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> response = postService.getAllPageByReview(reviewId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getAllPostsPagedByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> response = postService.getAllPageByUser(userId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<PostResponse>> getPostsByReviewId(@PathVariable String reviewId) {
        List<PostResponse> response = postService.getByReviewId(reviewId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable String userId) {
        List<PostResponse> response = postService.getByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<PostResponse> likePost(
            @PathVariable String id,
            @RequestParam String userId) {
        PostResponse response = postService.likePost(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String id,
            @RequestBody PostRequest request,
            @RequestParam String userId) {
        PostResponse response = postService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<PostResponse> softDeletePost(
            @PathVariable String id,
            @RequestParam String userId) {
        PostResponse response = postService.softDeleteById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<MessageResponse> hardDeletePost(
            @PathVariable String id,
            @RequestParam String userId) {
        MessageResponse response = postService.hardDeleteById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}