package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @Valid @RequestBody ReviewRequest request,
            @RequestParam String userId) {
        return ResponseEntity.ok(reviewService.create(request, userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }


    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }


    @GetMapping("/page")
    public ResponseEntity<Page<ReviewResponse>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getAllPage(page, size));
    }


    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponse>> getByBookId(@PathVariable String bookId) {
        return ResponseEntity.ok(reviewService.getByBookId(bookId));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(reviewService.getByUserId(userId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest request,
            @RequestParam String userId) {
        return ResponseEntity.ok(reviewService.update(id, request, userId));
    }


    @DeleteMapping("/soft/{id}")
    public ResponseEntity<ReviewResponse> softDelete(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(reviewService.softDeleteById(id, userId));
    }


    @DeleteMapping("/hard/{id}")
    public ResponseEntity<MessageResponse> hardDelete(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(reviewService.hardDeleteById(id, userId));
    }
}