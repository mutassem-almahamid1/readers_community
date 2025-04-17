package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User mapToDocument(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .profilePicture(request.getProfilePicture())
                .bio(request.getBio())
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse mapToResponse(User document) {
        return UserResponse.builder()
                .id(document.getId())
                .username(document.getUsername())
                .profilePicture(document.getProfilePicture())
                .bio(document.getBio())
                .role(document.getRole())
                .createdAt(document.getCreatedAt())
                .build();
    }

}
