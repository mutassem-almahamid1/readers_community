package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.dto.response.BookResponseWithDetails;
import com.project.readers_community.model.dto.response.CategoryResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.repository.CategoryRepo;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class BookMapper {
    public static Book mapToDocument(BookRequest request, String addedBy) {
        return Book.builder()
                .title(request.getTitle().trim())
                .author(request.getAuthor().trim())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .category(request.getCategory() != null ? request.getCategory().trim() : null)
                .coverImage(request.getCoverImageUrl() != null ? request.getCoverImageUrl().trim() : null)
                .addedBy(addedBy.trim())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookResponse mapToResponse(Book document) {
        return BookResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .author(document.getAuthor())
                .description(document.getDescription())
                .categoryId(document.getCategory())
                .coverImage(document.getCoverImage())
                .reviewCount(document.getReviewCount())
                .readerCount(document.getReaderCount())
                .avgRating(document.getAvgRating())
                .addedById(document.getAddedBy())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .readerCount(document.getReaderCount())
                .build();
    }

    public static BookResponseWithDetails mapToResponseWithDetails(Book document, CategoryResponse category, UserResponse user) {
        return BookResponseWithDetails.builder()
                .id(document.getId())
                .title(document.getTitle())
                .author(document.getAuthor())
                .description(document.getDescription())
                .category(category)
                .coverImage(document.getCoverImage())
                .reviewCount(document.getReviewCount())
                .readerCount(document.getReaderCount())
                .avgRating(document.getAvgRating())
                .addedBy(user)
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .readerCount(document.getReaderCount())
                .build();
    }
}
