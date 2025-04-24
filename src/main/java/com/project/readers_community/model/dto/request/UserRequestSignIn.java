package com.project.readers_community.model.dto.request;

import com.project.readers_community.model.document.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestSignIn {
    @NotBlank(message = "Username is required")
    @NotNull(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String profilePicture;

    private String bio;

    @NotNull( message = "Role is required")
    private Roles role;
}
