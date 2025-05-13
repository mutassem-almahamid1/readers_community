package com.project.readers_community.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
   @NotBlank(message = "Content is required")
   @Size(max = 500, message = "Comment content must be 500 characters or less")
   private String content;
}
