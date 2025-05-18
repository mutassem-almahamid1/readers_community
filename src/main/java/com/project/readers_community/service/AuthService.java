package com.project.readers_community.service;

import com.project.readers_community.model.dto.request.JwtResponse;
import com.project.readers_community.model.dto.request.LoginRequest;
import com.project.readers_community.model.dto.request.SignupRequest;
import com.project.readers_community.model.dto.request.TokenRefreshRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
   @Transactional
   UserResponse registerUser(SignupRequest signupRequest);

   @Transactional
   JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response);

   void addTokenToHeader(HttpServletResponse response, String jwtToken, String refreshToken);

   @Transactional
   JwtResponse refreshToken(TokenRefreshRequest request);

   @Transactional
   void logoutUser(String email, @NotNull HttpServletRequest request, HttpServletResponse response);
}
