package com.project.readers_community.model.document;

import com.project.readers_community.model.enums.Roles;
import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String profilePicture;
    private String coverPicture;
    private String bio;
    private Roles role;

    @Builder.Default
    private List<String> wantToReadBooks = new ArrayList<>();
    @Builder.Default
    private List<String> currentlyReadingBooks = new ArrayList<>();
    @Builder.Default
    private List<String> finishedBooks = new ArrayList<>();
    @Builder.Default
    private List<String> followers = new ArrayList<>();
    @Builder.Default
    private List<String> following = new ArrayList<>();

    private String refreshToken;
    private LocalDateTime refreshTokenExpiryDate;

    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
