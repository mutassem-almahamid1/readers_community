package com.project.readers_community.controller;

import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/review")
    public ResponseEntity<CommentResponse> createCommentOnReview(
            @RequestBody CommentRequest request,
            @RequestParam String userId) {
        CommentResponse response = commentService.createCommentOnReview(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/post")
    public ResponseEntity<CommentResponse> createCommentOnPost(
            @RequestBody CommentRequest request,
            @RequestParam String userId) {
        CommentResponse response = commentService.createCommentOnPost(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable String id) {
        CommentResponse response = commentService.getCommentById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByReviewId(@PathVariable String reviewId) {
        List<CommentResponse> response = commentService.getByReviewId(reviewId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/review/{reviewId}/paged")
    public ResponseEntity<Page<CommentResponse>> getCommentsByReviewIdPaged(
            @PathVariable String reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentResponse> response = commentService.getByReviewIdPaged(reviewId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable String postId) {
        List<CommentResponse> response = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/paged")
    public ResponseEntity<Page<CommentResponse>> getCommentsByPostIdPaged(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentResponse> response = commentService.getCommentsByPostIdPaged(postId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<CommentResponse> likeComment(
            @PathVariable String id,
            @RequestParam String userId) {
        CommentResponse response = commentService.likeComment(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable String id,
            @RequestBody CommentRequest request,
            @RequestParam String userId) {
        CommentResponse response = commentService.update(id,request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{id}/softDelet")
    public ResponseEntity<Void> softDeleteComment(
            @PathVariable String id,
            @RequestParam String userId) {
        commentService.softDeleteById(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/hardDelet")
    public ResponseEntity<Void> hardDeleteComment(
            @PathVariable String id,
            @RequestParam String userId) {
        commentService.hardDeleteById(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}