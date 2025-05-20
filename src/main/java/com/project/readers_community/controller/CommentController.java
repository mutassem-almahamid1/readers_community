package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.UpdateCommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createCommentOnReview(
            @Valid @RequestBody CommentRequest request,
            @RequestParam String userId) {
        CommentResponse response = commentService.createCommentOnReview(request, userId);
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

//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable String postId) {
//        List<CommentResponse> response = commentService.getCommentsByPostId(postId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/post/{postId}/paged")
//    public ResponseEntity<Page<CommentResponse>> getCommentsByPostIdPaged(
//            @PathVariable String postId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Page<CommentResponse> response = commentService.getCommentsByPostIdPaged(postId, page, size);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping("/{id}/like")
    public ResponseEntity<MessageResponse> likeComment(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(this.commentService.likeComment(id, userId));
    }

//    @PutMapping("/{id}/edit")
//    public ResponseEntity<MessageResponse> editComment(
//            @PathVariable String id,
//            @RequestBody UpdateCommentRequest request,
//            @RequestParam String userId) {
//        return ResponseEntity.ok(this.commentService.update(id, request, userId));
//    }


    @DeleteMapping("/{id}/soft")
    public ResponseEntity<MessageResponse> softDeleteComment(
            @PathVariable String id,
            @RequestParam String userId) {
        return  ResponseEntity.ok(commentService.softDeleteById(id, userId));
    }

//    @DeleteMapping("/{id}/hard")
//    public ResponseEntity<MessageResponse> hardDeleteComment(
//            @PathVariable String id,
//            @RequestParam String userId) {
//        return  ResponseEntity.ok(commentService.hardDeleteById(id, userId));
//    }


}