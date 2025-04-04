package com.project.readers_community.dto;


import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String username;
    private String role;
    private String email;



    public LoginResponseDTO(String token, String username,String email, String role) {
        this.token = token;
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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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