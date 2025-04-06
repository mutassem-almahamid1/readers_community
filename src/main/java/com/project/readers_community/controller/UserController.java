package com.project.readers_community.controller;

import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;

import com.project.readers_community.entity.User;
import com.project.readers_community.service.user_service.UserServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImp userServiceImp;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User registeredUser = userServiceImp.registerUser(registrationDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            logger.error("User registration failed", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        try {
            logger.info("Attempting login for user with email: {}", loginDTO.getEmail());
            UserLoginResponseDTO response = userServiceImp.loginUser(loginDTO);
            return ResponseEntity.ok(response);
        }

        catch (RuntimeException e) {
                logger.error("Login failed for user: {}", loginDTO.getEmail(), e);
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Login failed: " + e.getMessage());
            }
    }

}

