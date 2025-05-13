package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.request.UpdateReviewRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody ReviewRequest request,
            @RequestParam String userId) {
        ReviewResponse response = reviewService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable String id) {
        ReviewResponse response = reviewService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allReviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> response = reviewService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged/allReviews")
    public ResponseEntity<Page<ReviewResponse>> getAllReviewsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> response = reviewService.getAllPage(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged/book/{bookId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByBookIdPaged(
            @PathVariable String bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> response = reviewService.getByBookIdPage(bookId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paged/user/{userId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUserIdPaged(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> response = reviewService.getByUserIdPage(userId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByBookId(@PathVariable String bookId) {
        List<ReviewResponse> response = reviewService.getByBookId(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable String userId) {
        List<ReviewResponse> response = reviewService.getByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ReviewResponse> likeReview(
            @PathVariable String id,
            @RequestParam String userId) {
        ReviewResponse response = reviewService.likeReview(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable String id,
            @RequestBody UpdateReviewRequest request,
            @RequestParam String userId) {
        ReviewResponse response = reviewService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<MessageResponse> softDeleteReview(
            @PathVariable String id,
            @RequestParam String userId) {
        MessageResponse response = reviewService.softDeleteById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<MessageResponse> hardDeleteReview(
            @PathVariable String id,
            @RequestParam String userId) {
        MessageResponse response = reviewService.hardDeleteById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}