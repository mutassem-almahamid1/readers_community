package com.project.readers_community.model.dto.request;

import com.project.readers_community.model.document.Roles;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestSignIn {

    @NotBlank(message = "Full is required")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 50, message = "Email must be between 3 and 50 characters")
    @Email(message = "Email should be valid")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String profilePicture;

    private String bio;

    @NotBlank( message = "Role is required")
    private Roles role;
}
