package com.project.readers_community.controller;

import com.project.readers_community.dto.UserLoginDTO;
import com.project.readers_community.dto.UserRegistrationDTO;
import com.project.readers_community.dto.LoginResponseDTO;
import com.project.readers_community.dto.AddBookToListDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;
import com.project.readers_community.service.RecommendationService;
import com.project.readers_community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User registeredUser = userService.registerUser(registrationDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        try {
            LoginResponseDTO response = userService.loginUser(loginDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Login failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<?> getRecommendations(@PathVariable String userId) {
        try {
            logger.info("Fetching recommendations for userId: {}", userId);
            List<Book> recommendations = recommendationService.getRecommendations(userId);
            return new ResponseEntity<>(recommendations, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Failed to fetch recommendations for userId {}: {}", userId, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/add-book-to-list")
    public ResponseEntity<?> addBookToList(@PathVariable String userId, @RequestBody AddBookToListDTO addBookDTO) {
        try {
            logger.info("Adding book '{}' to list {} for userId: {}", addBookDTO.getBookTitle(), addBookDTO.getListType(), userId);
            User updatedUser = userService.addBookToList(userId, addBookDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Failed to add book to list for userId {}: {}", userId, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}