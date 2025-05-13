package com.project.readers_community.model.dto.response;


import com.project.readers_community.model.document.Roles;
import com.project.readers_community.model.document.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String username;
    private String profilePicture;
    private String bio;
    private Roles role;
    private List<String> followers;
    private List<String> following;
    private String email;
    private Status status;
    private LocalDateTime createdAt;
}
