package com.project.readers_community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  {

        @Id
        private String id;
        @Indexed(unique = true)
        private String username;
        private String password;
        private String profilePicture;
        private String bio;
        private Roles role;
        private List<String> wantToReadBooks=new ArrayList<>(); // Book objects
        private List<String> currentlyReadingBooks = new ArrayList<>(); // Book objects being currently read
        private List<String> finishedBooks = new ArrayList<>(); // Book objects that have been finished
        private List<String> followers = new ArrayList<>(); // User IDs
        private List<String> following = new ArrayList<>(); // User IDs
        private Status status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
}
