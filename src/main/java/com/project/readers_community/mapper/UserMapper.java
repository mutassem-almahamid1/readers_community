package com.project.readers_community.mapper;

import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.dto.request.SignupRequest;
import com.project.readers_community.model.enums.Roles;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

public class UserMapper {
    public static User mapToDocument(UserRequestSignIn request) {
        return User.builder()
                .fullName(AssistantHelper.trimString(request.getFullName()))
                .username(AssistantHelper.trimString(request.getUsername()))
                .email(AssistantHelper.trimString(request.getEmail()))
                .password(request.getPassword())
                .profilePicture(AssistantHelper.trimString(request.getProfilePicture()))
                .coverPicture(AssistantHelper.trimString(request.getCoverPicture()))
                .bio(request.getBio() != null ? request.getBio().trim() : null)
                .role(request.getRole())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User mapToDocument(SignupRequest request) {
        return User.builder()
                .fullName(AssistantHelper.trimString(request.getFullName()))
                .username(AssistantHelper.trimString(request.getUsername()))
                .email(AssistantHelper.trimString(request.getEmail()))
                .password(request.getPassword())
                .profilePicture(AssistantHelper.trimString(request.getProfilePicture()))
                .coverPicture(AssistantHelper.trimString(request.getCoverPicture()))
                .bio(request.getBio() != null ? request.getBio().trim() : null)
                .role(Roles.USER)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User mapToDocument(UserRequestLogin request) {
        return User.builder()
                .email(request.getEmail().trim())
                .password(request.getPassword())
                .build();
    }

    public static UserResponse mapToResponse(User document) {
        return UserResponse.builder()
                .id(document.getId())
                .fullName(document.getFullName())
                .username(document.getUsername())
                .email(document.getEmail())
                .profilePicture(document.getProfilePicture())
                .coverPicture(document.getCoverPicture())
                .bio(document.getBio())
                .followers(document.getFollowers())
                .following(document.getFollowing())
                .currentlyReadingBooks(document.getCurrentlyReadingBooks())
                .finishedBooks(document.getFinishedBooks())
                .wantToReadBooks(document.getWantToReadBooks())
                .readingBookCount(document.getFinishedBooks().size())
                .followersCount(document.getFollowers().size())
                .followingCount(document.getFollowing().size())
                .role(document.getRole())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
