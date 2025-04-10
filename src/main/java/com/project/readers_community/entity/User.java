package com.project.readers_community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

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
        private List<String> wantToRead; // معرفات الكتب
        private List<String> currentlyReading;
        private List<String> finishedReading;
        private List<String> followers; // User IDs
        private List<String> following; // User IDs
        private LocalDateTime createdAt;
        private List<String> recentActivities;

}