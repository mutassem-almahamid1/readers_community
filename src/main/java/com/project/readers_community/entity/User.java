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
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getWantToRead() {
        return wantToRead;
    }

    public void setWantToRead(List<String> wantToRead) {
        this.wantToRead = wantToRead;
    }

    public List<String> getCurrentlyReading() {
        return currentlyReading;
    }

    public void setCurrentlyReading(List<String> currentlyReading) {
        this.currentlyReading = currentlyReading;
    }

    public List<String> getFinishedReading() {
        return finishedReading;
    }

    public void setFinishedReading(List<String> finishedReading) {
        this.finishedReading = finishedReading;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<String> recentActivities) {
        this.recentActivities = recentActivities;
    }
}