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
public class User implements Principal, UserDetails {

        @Id
        private String id;
        @Indexed(unique = true)
        private String username;
        @Indexed(unique = true)
        private String email;
        private String password;
        @DBRef
        private List<Role> roles = new ArrayList<>();
        private String profilePicture;
        private String bio;
        private List<Book> wantToReadBooks=new ArrayList<>(); // Book objects
        private List<Book> currentlyReadingBooks = new ArrayList<>(); // Book objects being currently read
        private List<Book> finishedBooks = new ArrayList<>(); // Book objects that have been finished
        private List<String> followers = new ArrayList<>(); // User IDs
        private List<String> following = new ArrayList<>(); // User IDs
        private LocalDateTime createdAt;
        private List<String> recentActivities = new ArrayList<>();
        private List<Review> reviews = new ArrayList<>();

        // Add a role to user and update role's users list
        public void addRole(Role role) {
            if (this.roles == null) {
                this.roles = new ArrayList<>();
            }
            if (!this.roles.contains(role)) {
                this.roles.add(role);
                role.addUser(this);
            }
        }

        @Override
        public String getName() {
                return username;
        }

        @Override
        public boolean implies(Subject subject) {
                return Principal.super.implies(subject);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return this.roles
                        .stream()
                        .map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                        .collect(Collectors.toList());
        }

}
