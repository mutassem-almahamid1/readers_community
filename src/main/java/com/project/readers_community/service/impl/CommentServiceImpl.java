package com.project.readers_community.service.impl;

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

        Review review = reviewRepo.getById(request.getReviewId());

        Comment comment = commentMapper.mapToDocument(request, user, review, null);
        Comment savedComment = commentRepo.save(comment);

        // إنشاء إشعار إذا كان المستخدم مختلفًا عن صاحب المراجعة
        if (!review.getUser().getId().equals(userId)) {
            String message = user.getUsername() + " commented on your review.";
            String bookId = review.getBook() != null ? review.getBook().getId() : null;
            notificationService.createNotificationAsync(
                    review.getUser().getId(),
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
        Post post = postRepo.getById(request.getReviewId());

        Comment comment = commentMapper.mapToDocument(request, user, null, post);
        Comment savedComment = commentRepo.save(comment);

        // إنشاء إشعار إذا كان المستخدم مختلفًا عن صاحب المنشور
        if (!post.getUser().getId().equals(userId)) {
            String message = user.getUsername() + " commented on your post.";
            notificationService.createNotificationAsync(
                    post.getUser().getId(),
                    userId,
                    NotificationType.COMMENT_ON_REVIEW,
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
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own comments");
        }
        commentMapper.updateDocument(comment, request);
        Comment updatedComment = commentRepo.save(comment);
        return commentMapper.mapToResponse(updatedComment, userId);
    }

    @Override
    public CommentResponse softDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own comments");
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
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own comments");
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
        comment.getLikedBy().add(user);
        comment.setLikeCount(comment.getLikeCount() + 1);
        Comment updatedComment = commentRepo.save(comment);

        // إنشاء إشعار إذا كان المستخدم مختلفًا عن صاحب التعليق
        if (!comment.getUser().getId().equals(userId)) {
            String message = user.getUsername() + " liked your comment.";
            String reviewId = comment.getReview() != null ? comment.getReview().getId() : null;
            String postId = comment.getPost()!= null? comment.getPost().getId() : null;
            notificationService.createNotificationAsync(
                    comment.getUser().getId(),
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