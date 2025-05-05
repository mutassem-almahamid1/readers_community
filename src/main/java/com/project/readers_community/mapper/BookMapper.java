package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookMapper {

    public Book mapToDocument(BookRequest request, Category category, User addedBy) {
        return Book.builder()
                .title(request.getTitle().trim())
                .author(request.getAuthor().trim())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .category(category)
                .coverImage(request.getCoverImageUrl() != null ? request.getCoverImageUrl().trim() : null)
                .addedBy(addedBy)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public BookResponse mapToResponse(Book document) {
        return BookResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .author(document.getAuthor())
                .description(document.getDescription())
                .categoryId(document.getCategory() != null ? document.getCategory().getId() : null)
                .categoryName(document.getCategory() != null ? document.getCategory().getName() : null)
                .coverImage(document.getCoverImage())
                .reviewCount(document.getReviewCount())
                .readerCount(document.getReaderCount())
                .avgRating(document.getAvgRating())
                .addedById(document.getAddedBy() != null ? document.getAddedBy().getId() : null)
                .addedByUsername(document.getAddedBy() != null ? document.getAddedBy().getUsername() : null)
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .readerCount(document.getReaderCount())
                .build();
    }
}
