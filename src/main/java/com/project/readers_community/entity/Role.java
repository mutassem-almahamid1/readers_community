package com.project.readers_community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    public static final String USER_ROLE = "USER";
    
    @Id
    private String id;
    private String name;

    @JsonIgnore
    @DBRef
    private List<User> users = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Additional constructor for creating a role with just a name
    public Role(String name) {
        this.name = name;
    }
    
    // Constructor with name and user
    public Role(String name, User user) {
        this.name = name;
        this.users = new ArrayList<>();
        if (user != null) {
            this.users.add(user);
        }
    }
    
    // Add user to role
    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }
}
