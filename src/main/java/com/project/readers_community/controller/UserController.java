package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.UpdateUserRequest;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequestSignIn request) {
        return ResponseEntity.ok(this.service.signUp(request));
    }


//    @PostMapping("/login")
//    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequestLogin request) {
//        return ResponseEntity.ok(this.service.login(request));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getById(id));
    }

//    @GetMapping("/{id}/if-present")
//    public ResponseEntity<UserResponse> getUserByIdIfPresent(@PathVariable String id) {
//        return ResponseEntity.ok(this.service.getByIdIfPresent(id));
//    }

    @GetMapping("/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllByName(@RequestParam String name) {
        return ResponseEntity.ok(this.service.getAllByName(name));
    }

    @GetMapping("/name/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getByNamePage(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.service.getByNamePage(name, page, size));
    }

//    @GetMapping("/users/{username}/if-present")
//    public ResponseEntity<UserResponse> getUserByUsernameIfPresent(@PathVariable String username) {
//        return ResponseEntity.ok(this.service.getByUsernameIfPresent(username));
//    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(this.service.getByAll());
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUserByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    @PatchMapping("/active/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> active(@PathVariable String id) {
        return ResponseEntity.ok(this.service.updateStatus(id, Status.ACTIVE));
    }

    @PatchMapping("/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> block(@PathVariable String id) {
        return ResponseEntity.ok(this.service.updateStatus(id, Status.BLOCKED));
    }

    @DeleteMapping("/soft/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> softDeleteUserById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.softDeleteById(id));
    }

//    @DeleteMapping("/hard/{id}")
//    public ResponseEntity<MessageResponse> hardDeleteUserById(@PathVariable String id) {
//        return ResponseEntity.ok(this.service.hardDeleteById(id));
//    }

    @GetMapping("/{userId}/want-to-read")
    public ResponseEntity<List<BookResponse>> getListWantToReadBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getWantToReadBooks(userId));
    }

    @GetMapping("/{userId}/finished")
    public ResponseEntity<List<BookResponse>> getListFinishedBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getFinishedBooks(userId));
    }

    @GetMapping("/{userId}/currently-reading")
    public ResponseEntity<List<BookResponse>> getListCurrentlyReadingBooks(@PathVariable String userId) {
        return ResponseEntity.ok(this.service.getCurrentlyReadingBooks(userId));
    }


    @PostMapping("/{userId}/follow-unfollow/{followingId}")
    public ResponseEntity<MessageResponse> followUser(
            @PathVariable String userId,
            @PathVariable String followingId) {
        return new ResponseEntity<>(service.followUser(userId, followingId), HttpStatus.OK);
    }


    @PostMapping("/{userId}/books/finished")
    public ResponseEntity<MessageResponse> addBookToFinishedList(
            @PathVariable String userId,
            @RequestParam String bookId) {
        return ResponseEntity.ok(service.addBookToFinishedList(userId, bookId));
    }

    @PostMapping("/{userId}/books/want-to-read")
    public ResponseEntity<MessageResponse> addBookToWantToReadList(
            @PathVariable String userId,
            @RequestParam String bookId) {
        return ResponseEntity.ok(service.addBookToWantToReadList(userId, bookId));
    }

    @PostMapping("/{userId}/books/currently-reading")
    public ResponseEntity<MessageResponse> addBookToCurrentlyReadingList(
            @PathVariable String userId,
            @RequestParam String bookId) {
        return ResponseEntity.ok(service.addBookToCurrentlyReadingList(userId, bookId));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserResponse>> getAllFollowingById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getAllFollowingById(id));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserResponse>> getAllFollowersById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getAllFollowersById(id));
    }
}