package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.UserRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> sign_up(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(this.service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getByAll() {
        return ResponseEntity.ok(this.service.getByAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserResponse>> getByAllPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<UserResponse> softDeleteById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.softDeleteById(id));
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<MessageResponse> hardDeleteById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.hardDeleteById(id));
    }

    @GetMapping("/{userId}/want-to-read")
    public ResponseEntity<List<String>> getWantToReadBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getWantToReadBooks(userId));
    }
}