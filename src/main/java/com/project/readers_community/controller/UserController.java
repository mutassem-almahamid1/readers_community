package com.project.readers_community.controller;

import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;
import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;
import com.project.readers_community.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        try {
            UserLoginResponseDTO response = userService.loginUser(loginDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{userId}/want-to-read/{bookId}")
    public ResponseEntity<?> addBookToWantToRead(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.addBookToWantToRead(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book added to Want to Read list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to add book to Want to Read list"));
        }
    }
    
    @DeleteMapping("/{userId}/want-to-read/{bookId}")
    public ResponseEntity<?> removeBookFromWantToRead(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.removeBookFromWantToRead(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book removed from Want to Read list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to remove book from Want to Read list"));
        }
    }
    
    @GetMapping("/{userId}/want-to-read")
    public ResponseEntity<?> getWantToReadBooks(@PathVariable String userId) {
        try {
            List<Book> books = userService.getWantToReadBooks(userId);
            return ResponseEntity.ok(books);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{userId}/currently-reading/{bookId}")
    public ResponseEntity<?> addBookToCurrentlyReading(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.addBookToCurrentlyReading(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book added to Currently Reading list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to add book to Currently Reading list"));
        }
    }
    
    @DeleteMapping("/{userId}/currently-reading/{bookId}")
    public ResponseEntity<?> removeBookFromCurrentlyReading(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.removeBookFromCurrentlyReading(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book removed from Currently Reading list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to remove book from Currently Reading list"));
        }
    }
    
    @GetMapping("/{userId}/currently-reading")
    public ResponseEntity<?> getCurrentlyReadingBooks(@PathVariable String userId) {
        try {
            List<Book> books = userService.getCurrentlyReadingBooks(userId);
            return ResponseEntity.ok(books);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/finished-books/{bookId}")
    public ResponseEntity<?> addBookToFinishedBooks(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.addBookToFinishedBooks(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book added to Finished Books list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to add book to Finished Books list"));
        }
    }
    
    @DeleteMapping("/{userId}/finished-books/{bookId}")
    public ResponseEntity<?> removeBookFromFinishedBooks(@PathVariable String userId, @PathVariable String bookId) {
        boolean success = userService.removeBookFromFinishedBooks(userId, bookId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Book removed from Finished Books list"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to remove book from Finished Books list"));
        }
    }
    
    @GetMapping("/{userId}/finished-books")
    public ResponseEntity<?> getFinishedBooks(@PathVariable String userId) {
        try {
            List<Book> books = userService.getFinishedBooks(userId);
            return ResponseEntity.ok(books);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("GetAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("GetUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with ID: " + userId));
        }
    }
}
