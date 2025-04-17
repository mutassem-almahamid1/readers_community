package com.project.readers_community.model.dto.response;


import com.project.readers_community.model.document.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String profilePicture;
    private String bio;
    private Roles role;
    private LocalDateTime createdAt;
}
