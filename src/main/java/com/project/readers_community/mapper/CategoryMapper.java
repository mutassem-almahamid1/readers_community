package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.dto.request.CategoryRequest;
import com.project.readers_community.model.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {
    public Category mapToDocument(CategoryRequest request) {
        return Category.builder()
                .name(request.getName().trim())
                .description(request.getDescription().trim())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CategoryResponse mapToResponse(Category document) {
        return CategoryResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .description(document.getDescription())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
