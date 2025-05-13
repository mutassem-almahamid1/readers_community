package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.repository.CategoryRepo;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookMapper {

    @Autowired
    private CategoryRepo categoryRepository;

    @Autowired
    private UserRepo userRepository;

    public Book mapToDocument(BookRequest request, String addedBy) {

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

    public BookResponse mapToResponse(Book document) {
        String categoryName = null;
        if (document.getCategory() != null) {
            Category category = categoryRepository.getById(document.getCategory());
            if (category != null) {
                categoryName = category.getName();
            }
        }

        String addedByUsername = null;
        if (document.getAddedBy() != null) {
            User user = userRepository.getById(document.getAddedBy());
            if (user != null) {
                addedByUsername = user.getUsername();
            }
        }

        return BookResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .author(document.getAuthor())
                .description(document.getDescription())
                .categoryId(document.getCategory())
                .categoryName(categoryName)
                .coverImage(document.getCoverImage())
                .reviewCount(document.getReviewCount())
                .readerCount(document.getReaderCount())
                .avgRating(document.getAvgRating())
                .addedById(document.getAddedBy())
                .addedByUsername(addedByUsername)
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .readerCount(document.getReaderCount())
                .build();
    }
}
