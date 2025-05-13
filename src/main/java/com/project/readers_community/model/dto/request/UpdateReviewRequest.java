package com.project.readers_community.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewRequest {
   @NotNull(message = "Rating is required")
   @Min(value = 1, message = "Rating must be at least 1")
   @Max(value = 5, message = "Rating cannot exceed 5")
   private Integer rating;

   private String content;
}
