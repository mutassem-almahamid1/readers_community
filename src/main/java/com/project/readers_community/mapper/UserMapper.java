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
                .username(request.getUsername())
                .password(request.getPassword())
                .profilePicture(request.getProfilePicture())
                .bio(request.getBio())
                .role(request.getRole())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public User mapToDocument(UserRequestLogin request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse mapToResponse(User document) {
        return UserResponse.builder()
                .id(document.getId())
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
