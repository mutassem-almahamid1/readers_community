package com.project.readers_community.mapper;

import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.PostResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class PostMapper {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private BookRepo bookRepo;

    public Post mapToDocument(PostRequest request, String userId, String reviewId) {
        return Post.builder()
                .review(reviewId)
                .user(userId)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .likedBy(new HashSet<>())
                .comments(new ArrayList<>())
                .build();
    }

    public PostResponse mapToResponse(Post post, String userId) {
        boolean likedByCurrentUser = post.getLikedBy() != null && userId != null &&
                post.getLikedBy().contains(userId);

        // Get associated objects
        User user = userRepo.getById(post.getUser());
        Review review = reviewRepo.getById(post.getReview());
        Book book = review != null ? bookRepo.getById(review.getBook()) : null;

        return PostResponse.builder()
                .id(post.getId())
                .reviewId(post.getReview())
                .userName(user != null ? user.getUsername() : null)
                .userProfilePicture(user != null ? user.getProfilePicture() : null)
                .bookId(book != null ? book.getId() : null)
                .bookTitle(book != null ? book.getTitle() : null)
                .bookCoverImage(book != null ? book.getCoverImage() : null)
                .authorName(book != null ? book.getAuthor() : null)
                .content(review != null ? review.getContent() : null)
                .rating(review != null ? review.getRating() : 0)
                .likeCount(post.getLikedBy() != null ? post.getLikedBy().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .comments(post.getComments())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    public void updateDocument(Post post,  String reviewId) {
        post.setReview(reviewId);
        post.setUpdatedAt(LocalDateTime.now());
    }
}
