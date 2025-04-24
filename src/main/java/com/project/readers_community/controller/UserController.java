package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
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
    public ResponseEntity<UserResponse> sign_up(@Valid @RequestBody UserRequestSignIn request) {
        return ResponseEntity.ok(this.service.signUp(request));
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequestLogin request) {
        return ResponseEntity.ok(this.service.login(request));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getById(id));
    }

    @GetMapping("/{id}/if-present")
    public ResponseEntity<UserResponse> getUserByIdIfPresent(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getByIdIfPresent(id));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(this.service.getByUsername(username));
    }

    @GetMapping("/users/{username}/if-present")
    public ResponseEntity<UserResponse> getUserByUsernameIfPresent(@PathVariable String username) {
        return ResponseEntity.ok(this.service.getByUsernameIfPresent(username));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(this.service.getByAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserResponse>> getAllUserByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UserRequestSignIn request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<UserResponse> softDeleteUserById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.softDeleteById(id));
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<MessageResponse> hardDeleteUserById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.hardDeleteById(id));
    }

    @GetMapping("/{userId}/want-to-read")
    public ResponseEntity<List<String>> getListWantToReadBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getWantToReadBooks(userId));
    }

    @GetMapping("/{userId}/finished")
    public ResponseEntity<List<String>> getListFinishedBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getFinishedBooks(userId));
    }

    @GetMapping("/{userId}/currently-reading")
    public ResponseEntity<List<String>> getListCurrentlyReadingBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getCurrentlyReadingBooks(userId));
    }

}