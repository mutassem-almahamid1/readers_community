package com.project.readers_community.mapper;

import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.dto.request.CategoryRequest;
import com.project.readers_community.model.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class CategoryMapper {
    public static Category mapToDocument(CategoryRequest request) {
        return Category.builder()
                .name(AssistantHelper.trimString(request.getName()))
                .description(AssistantHelper.trimString(request.getDescription()))
                .status(Status.ACTIVE)
                .imgUrl(AssistantHelper.trimString(request.getImgUrl()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CategoryResponse mapToResponse(Category document) {
        return CategoryResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .description(document.getDescription())
                .imgUrl(document.getImgUrl())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
