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
    @NotBlank(message = "Name must be not blank.")
    private String name;
    @NotBlank(message = "description must be not blank.")
    private String description;
    @NotBlank(message = "imgUrl must be not blank.")
    private String imgUrl;
}
