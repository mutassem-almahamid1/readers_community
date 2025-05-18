package com.project.readers_community.model.dto.response;


import com.project.readers_community.model.enums.Roles;
import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String profilePicture;
    private String coverPicture;
    private String bio;
    private Roles role;
    private List<String> wantToReadBooks;
    private List<String> currentlyReadingBooks;
    private List<String> finishedBooks;
    private List<String> followers;
    private List<String> following;
    private Integer readingBookCount;
    private Integer followersCount;
    private Integer followingCount;
    private Status status;
    private LocalDateTime createdAt;
}
