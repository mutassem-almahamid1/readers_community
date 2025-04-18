package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.PostResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {

    public Post mapToDocument(PostRequest request, Review review) {
        String content = review.getContent() != null
                ? review.getContent().trim()
                : "Rated the book with " + review.getRating() + " stars";

        return Post.builder()
                .review(review)
                .user(review.getUser())
                .content(content)
                .rating(review.getRating())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .reviewId(post.getReview() != null ? post.getReview().getId() : null)
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .username(post.getUser() != null ? post.getUser().getUsername() : null)
                .bookId(post.getReview() != null && post.getReview().getBook() != null
                        ? post.getReview().getBook().getId()
                        : null)
                .bookTitle(post.getReview() != null && post.getReview().getBook() != null
                        ? post.getReview().getBook().getTitle()
                        : null)
                .content(post.getContent())
                .rating(post.getRating())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }
}