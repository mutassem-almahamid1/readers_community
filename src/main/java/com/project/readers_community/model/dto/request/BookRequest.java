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

    @NotNull(message = "Title is required.")
    @NotEmpty(message = "Title must be not empty.")
    @NotBlank(message = "Title must be not blank.")
    private String title;

    @NotNull(message = "Author is required.")
    @NotEmpty(message = "Author must be not empty.")
    @NotBlank(message = "Author must be not blank.")
    private String author;

    @NotNull(message = "Description is required.")
    @NotEmpty(message = "Description must be not empty.")
    @NotBlank(message = "Description must be not blank.")
    private String description;

    @NotNull(message = "category is required.")
    @NotEmpty(message = "category must be not empty.")
    @NotBlank(message = "category must be not blank.")
    private String categoryId;


    private String coverImageUrl;

}
