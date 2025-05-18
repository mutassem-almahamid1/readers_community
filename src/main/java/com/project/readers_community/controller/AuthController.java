package com.project.readers_community.controller;

import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.JwtResponse;
import com.project.readers_community.model.dto.request.LoginRequest;
import com.project.readers_community.model.dto.request.SignupRequest;
import com.project.readers_community.model.dto.request.TokenRefreshRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     *
     * @param signupRequest the signup request
     * @return response entity with user DTO
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse userDto = authService.registerUser(signupRequest);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Authenticate user
     *
     * @param loginRequest the login request
     * @return response entity with JWT response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Refresh token
     *
     * @param request the token refresh request
     * @return response entity with JWT response
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        JwtResponse jwtResponse = authService.refreshToken(request);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Logout user
     *
     * @return response entity with success message
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(@NotNull HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        authService.logoutUser(userDetails.getUsername(), request, response);
        return ResponseEntity.ok(AssistantHelper.toMessageResponse("User logged out successfully"));
    }

}
