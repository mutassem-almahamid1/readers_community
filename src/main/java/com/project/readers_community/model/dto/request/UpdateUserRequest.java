package com.project.readers_community.model.dto.request;

import com.project.readers_community.model.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
   @NotBlank(message = "Full is required")
   @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
   private String fullName;

   @NotBlank(message = "Username is required")
   @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
   private String username;

   private String profilePicture;
   private String coverPicture;

   private String bio;
}
