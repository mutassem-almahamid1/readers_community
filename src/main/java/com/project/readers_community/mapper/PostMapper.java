package com.project.readers_community.mapper;

import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.PostResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post mapToDocument(PostRequest request, User user, Review review) {
        return Post.builder()
                .review(review)
                .user(user)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .likedBy(new java.util.HashSet<>())
                .comments(new java.util.ArrayList<>())
                .build();
    }

    public PostResponse mapToResponse(Post post, String userId) {
        boolean likedByCurrentUser = post.getLikedBy() != null && userId != null &&
                post.getLikedBy().stream().anyMatch(user -> user.getId().equals(userId));

        Review review = post.getReview();
        return PostResponse.builder()
                .id(post.getId())
                .reviewId(review.getId())
                .userName(post.getUser() != null ? post.getUser().getUsername() : null)
                .userProfilePicture(post.getUser() != null ? post.getUser().getProfilePicture() : null) // افترضنا وجود حقل profilePicture في User
                .bookId(review.getBook() != null ? review.getBook().getId() : null)
                .bookTitle(review.getBook() != null ? review.getBook().getTitle() : null)
                .bookCoverImage(review.getBook() != null ? review.getBook().getCoverImage() : null) // افترضنا وجود حقل coverImage في Book
                .authorName(review.getBook() != null ? review.getBook().getAuthor() : null) // افترضنا وجود حقل authorName في Book
                .content(review.getContent())
                .rating(review.getRating())
                .likeCount(post.getLikedBy() != null ? post.getLikedBy().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .comments(post.getComments() != null ? post.getComments().stream()
                        .map(comment -> CommentResponse.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                                .createdAt(comment.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()) : new java.util.ArrayList<>())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public void updateDocument(Post post, PostRequest request, Review review) {
        post.setReview(review);
        post.setUpdatedAt(LocalDateTime.now());
    }

}