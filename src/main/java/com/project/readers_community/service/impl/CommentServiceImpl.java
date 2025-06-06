package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.ForbiddenException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.repository.CommentRepo;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.CommentService;
import com.project.readers_community.service.NotificationService;
import com.project.readers_community.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentResponse createCommentOnReview(CommentRequest request, String userId) {
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Review review = reviewRepo.getById(request.getReviewId());
        if (review == null) {
            throw new NotFoundException("Review not found");
        }

        Comment comment = commentMapper.mapToDocument(request, userId);
        Comment savedComment = commentRepo.save(comment);

        if (!review.getUser().equals(userId)) {
            String message = user.getUsername() + " commented on your review.";
            String bookId = review.getBook();
            notificationService.createNotificationAsync(
                    review.getUser(),
                    userId,
                    NotificationType.COMMENT_ON_REVIEW,
                    message,
                    review.getId(),
                    savedComment.getId(),
                    bookId,
                    null
            );
        }

        return commentMapper.mapToResponse(savedComment, userId);
    }

    @Override
    public CommentResponse createCommentOnPost(CommentRequest request, String userId) {
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        
        Post post = postRepo.getById(request.getPostId());
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        Comment comment = commentMapper.mapToDocument(request, userId);
        Comment savedComment = commentRepo.save(comment);

        if (!post.getUser().equals(userId)) {
            String message = user.getUsername() + " commented on your post.";
            notificationService.createNotificationAsync(
                    post.getUser(),
                    userId,
                    NotificationType.COMMENT_ON_POST,
                    message,
                    post.getId(),
                    savedComment.getId(),
                    null,
                    null
            );
        }

        return commentMapper.mapToResponse(savedComment, userId);
    }

    @Override
    public CommentResponse getCommentById(String id) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return commentMapper.mapToResponse(comment, null);
    }

    @Override
    public List<CommentResponse> getByReviewId(String reviewId) {
        List<Comment> comments = commentRepo.getByReviewId(reviewId);
        return comments.stream()
                .map(comment -> commentMapper.mapToResponse(comment, null))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentResponse> getByReviewIdPaged(String reviewId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepo.getByReviewIdPaged(reviewId, pageRequest);
        return comments.map(comment ->
                commentMapper.mapToResponse(comment, null));
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepo.getByPostId(postId);
        return comments.stream()
               .map(comment -> commentMapper.mapToResponse(comment, null))
               .collect(Collectors.toList());
    }

    @Override
    public Page<CommentResponse> getCommentsByPostIdPaged(String postId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepo.getByPostIdPaged(postId, pageRequest);
        return comments.map(comment ->
                commentMapper.mapToResponse(comment, null));
    }

    @Override
    public List<CommentResponse> getByUserId(String userId) {
        List<Comment> comments = commentRepo.getByUserId(userId);
        return comments.stream()
                .map(comment -> commentMapper.mapToResponse(comment, null))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse update(String id, CommentRequest request, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().equals(userId)) {
            throw new ForbiddenException("You can only update your own comments");
        }
        commentMapper.updateDocument(comment, request);
        Comment updatedComment = commentRepo.save(comment);
        return commentMapper.mapToResponse(updatedComment, userId);
    }

    @Override
    public CommentResponse softDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own comments");
        }
        comment.setStatus(Status.DELETED);
        comment.setDeletedAt(LocalDateTime.now());
        Comment updatedComment = commentRepo.save(comment);
        return commentMapper.mapToResponse(updatedComment, userId);
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own comments");
        }
        commentRepo.deleteById(id);
        return MessageResponse.builder().message("Comment deleted successfully").build();
    }

    @Override
    public CommentResponse likeComment(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        
        if (comment.getLikedBy() == null) {
            comment.setLikedBy(new HashSet<>());
        }
        
        comment.getLikedBy().add(userId);
        comment.setLikeCount(comment.getLikedBy().size());
        Comment updatedComment = commentRepo.save(comment);


        if (!comment.getUser().equals(userId)) {
            String message = user.getUsername() + " liked your comment.";
            String reviewId = comment.getReview();
            String postId = comment.getPost();
            notificationService.createNotificationAsync(
                    comment.getUser(),
                    userId,
                    NotificationType.LIKE_COMMENT,
                    message,
                    reviewId,
                    comment.getId(),
                    null,
                    postId
            );
        }

        return commentMapper.mapToResponse(updatedComment, userId);
    }
}
