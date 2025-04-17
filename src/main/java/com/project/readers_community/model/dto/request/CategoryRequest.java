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
public class CategoryRequest {
    @NotNull(message = "Name is required.")
    @NotEmpty(message = "Name must be not empty.")
    @NotBlank(message = "Name must be not blank.")
    private String name;
    @NotNull(message = "description is required.")
    @NotEmpty(message = "description must be not empty.")
    @NotBlank(message = "description must be not blank.")
    private String description;

}
