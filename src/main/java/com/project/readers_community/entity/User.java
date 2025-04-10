package com.project.readers_community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

        @Id
        private String id;
        private String username;
        @Indexed(unique = true)
        private String email;
        private String password;
        private String role;
        private String profilePicture;
        private String bio;
        @DBRef
        private List<Book> wantToReadBooks=new ArrayList<>(); // Book objects
        @DBRef
        private List<Book> currentlyReadingBooks = new ArrayList<>(); // Book objects being currently read
        @DBRef
        private List<Book> finishedBooks = new ArrayList<>(); // Book objects that have been finished
        private List<String> followers; // User IDs
        private List<String> following; // User IDs
        private LocalDateTime createdAt;
        private List<String> recentActivities;

}
