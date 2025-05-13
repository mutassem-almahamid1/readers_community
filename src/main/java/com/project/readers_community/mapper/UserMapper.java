package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User mapToDocument(UserRequestSignIn request) {
        return User.builder()
                .fullName(request.getFullName().trim())
                .username(request.getUsername().trim())
                .email(request.getEmail().trim())
                .password(request.getPassword())
                .profilePicture(request.getProfilePicture() != null ? request.getProfilePicture().trim() : null)
                .bio(request.getBio() != null ? request.getBio().trim() : null)
                .role(request.getRole())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public User mapToDocument(UserRequestLogin request) {
        return User.builder()
                .email(request.getEmail().trim())
                .password(request.getPassword())
                .build();
    }

    public UserResponse mapToResponse(User document) {
        return UserResponse.builder()
                .id(document.getId())
                .fullName(document.getFullName())
                .username(document.getUsername())
                .profilePicture(document.getProfilePicture())
                .bio(document.getBio())
                .followers(document.getFollowers())
                .following(document.getFollowing())
                .role(document.getRole())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .build();
    }



}
