package com.project.readers_community.dto.user_dto;


import lombok.Data;

@Data
public class UserLoginResponseDTO {

    private String username;
    private String role;
    private String email;



    public UserLoginResponseDTO(String username, String email, String role) {

        this.username = username;
        this.role = role;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}