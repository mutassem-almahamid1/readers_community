package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.PostResponse;
import com.project.readers_community.service.PostService;
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
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;



    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request,
            @RequestParam String userId) {
        return ResponseEntity.ok(postService.create(request, userId));
    }



    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(postService.getById(id));
    }


    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }



    @GetMapping("/page")
    public ResponseEntity<Page<PostResponse>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPage(page, size));
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(postService.getByUserId(userId));
    }



    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<PostResponse>> getByReviewId(@PathVariable String reviewId) {
        return ResponseEntity.ok(postService.getByReviewId(reviewId));
    }



    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PostRequest request,
            @RequestParam String userId) {
        return ResponseEntity.ok(postService.update(id, request, userId));
    }



    @DeleteMapping("/soft/{id}")
    public ResponseEntity<PostResponse> softDelete(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(postService.softDeleteById(id, userId));
    }



    @DeleteMapping("/hard/{id}")
    public ResponseEntity<MessageResponse> hardDelete(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(postService.hardDeleteById(id, userId));
    }
}