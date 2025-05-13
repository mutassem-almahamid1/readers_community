package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseWithDetails {
   private String id;
   private String title;
   private String author;
   private String description;
   private CategoryResponse category;
   private String coverImage;
   private int reviewCount;
   private double avgRating;
   private int readerCount;
   private UserResponse addedBy;

   private Status status;
   private LocalDateTime createdAt;
}
