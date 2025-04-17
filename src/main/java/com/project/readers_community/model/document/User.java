package com.project.readers_community.model.document;

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


@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String profilePicture;
    private String bio;
    private Roles role = Roles.USER; // Default role is USER
    private List<String> wantToReadBooks = new ArrayList<>(); //Book IDs
    private List<String> currentlyReadingBooks = new ArrayList<>(); // Book IDs
    private List<String> finishedBooks = new ArrayList<>(); // Book IDs
    private List<String> followers = new ArrayList<>(); // User IDs
    private List<String> following = new ArrayList<>(); // User IDs
    private Status status= Status.ACTIVE; // Default status is ACTIVE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
