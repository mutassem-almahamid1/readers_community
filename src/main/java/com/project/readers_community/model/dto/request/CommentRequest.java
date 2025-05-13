package com.project.readers_community.model.dto.request;

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
public class CommentRequest {
    @NotBlank(message = "Content is required")
    private String reviewId;
//    private String text;
//    private String postId;
    @NotBlank(message = "Content is required")
    @Size(max = 500, message = "Comment content must be 500 characters or less")
    private String content;
}