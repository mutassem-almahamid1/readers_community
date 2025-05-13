package com.project.readers_community.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {
    @NotBlank(message = "Title must be not blank.")
    private String title;
    @NotBlank(message = "Author must be not blank.")
    private String author;
    @NotBlank(message = "Description must be not blank.")
    private String description;
    @NotBlank(message = "category must be not blank.")
    private String category;
    @NotBlank(message = "coverImageUrl must be not blank.")
    private String coverImageUrl;
}
