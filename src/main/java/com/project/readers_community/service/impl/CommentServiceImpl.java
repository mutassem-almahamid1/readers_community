package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.request.UpdateCommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.repository.CommentRepo;

import com.project.readers_community.service.*;
import com.project.readers_community.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Override
    public CommentResponse createCommentOnReview(CommentRequest request, String userId) {
        Comment comment = CommentMapper.mapToDocument(request, userId);
        UserResponse user = userService.getById(userId);
        ReviewResponse review = reviewService.getById(request.getReviewId());
        Comment savedComment = commentRepo.save(comment);

        if (!review.getUserId().equals(userId)) {
            NotificationResponse notificationResponse = notificationService.create(NotificationRequest.builder()
                            .recipientId(review.getUserId())
                            .triggerUserId(userId)
                            .message(user.getUsername() + " commented on your review.")
                            .reviewId(review.getId())
                            .bookId(review.getBookId())
                            .commentId(savedComment.getId())
                            .type(NotificationType.COMMENT_ON_REVIEW)
                    .build());

            this.webSocketNotificationService.notifyNewNotification(notificationResponse, review.getUserId());
        }

        return CommentMapper.mapToResponse(savedComment);
    }

    @Override
    public CommentResponse getCommentById(String id) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        List<UserResponse> userResponses = this.userService.getAllByIdIn(List.of(comment.getUser()));
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return CommentMapper.mapToResponse(comment, userResponseMap);
    }

    @Override
    public List<CommentResponse> getByReviewId(String reviewId) {
        List<Comment> comments = commentRepo.getByReviewId(reviewId);
        List<UserResponse> userResponses = this.userService.getAllByIdIn(comments.stream().map(Comment::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return comments.stream()
                .map(comment -> CommentMapper.mapToResponse(comment, userResponseMap))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentResponse> getByReviewIdPaged(String reviewId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepo.getByReviewIdPaged(reviewId, pageRequest);
        List<UserResponse> userResponses = this.userService.getAllByIdIn(comments.stream().map(Comment::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return comments.map(comment -> CommentMapper.mapToResponse(comment, userResponseMap));
    }

    @Override
    public List<CommentResponse> getByAllByIdIn(List<String> ids) {
        List<Comment> comments = commentRepo.getAllByIdIn(ids);
        return comments.stream()
                .map(CommentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getByUserId(String userId) {
        List<Comment> comments = commentRepo.getByUserId(userId);
        return comments.stream()
                .map(CommentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse update(String id, UpdateCommentRequest request, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        if (!comment.getUser().equals(userId)) {
            throw new BadReqException("You can only update your own comments");
        }
        CommentMapper.updateDocument(comment, request);
        Comment updatedComment = commentRepo.save(comment);
        return AssistantHelper.toMessageResponse("Comment updated successfully");
    }

    @Override
    public MessageResponse softDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        if (!comment.getUser().equals(userId)) {
            throw new BadReqException("You can only delete your own comments");
        }
        comment.setStatus(Status.DELETED);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepo.save(comment);
        return AssistantHelper.toMessageResponse("Comment deleted successfully");
    }

    @Override
    public MessageResponse softDeleteByReviewId(String reviewId) {
        List<Comment> comments = commentRepo.getByReviewId(reviewId);
        comments.forEach(comment -> {
            comment.setStatus(Status.DELETED);
            comment.setDeletedAt(LocalDateTime.now());
        });
        commentRepo.saveAll(comments);
        return AssistantHelper.toMessageResponse("Comment deleted successfully");
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        if (!comment.getUser().equals(userId)) {
            throw new BadReqException("You can only delete your own comments");
        }
        commentRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Comment deleted successfully");
    }

    @Override
    public MessageResponse likeComment(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        UserResponse user = userService.getById(userId);
        
        if (comment.getLikedBy() == null) {
            comment.setLikedBy(new HashSet<>());
        }

        boolean liked = true;
        if (comment.getLikedBy().contains(userId)){
            liked = false;
            comment.getLikedBy().remove(userId);
        } else {
            comment.getLikedBy().add(userId);
        }

        comment.setLikeCount(comment.getLikedBy().size());
        Comment updatedComment = commentRepo.save(comment);


        if (!comment.getUser().equals(userId) && liked) {
            NotificationResponse notificationResponse = notificationService.create(NotificationRequest.builder()
                            .recipientId(comment.getUser())
                            .triggerUserId(userId)
                            .message(user.getUsername() + " liked your comment.")
                            .reviewId(comment.getReview())
                            .commentId(comment.getId())
                            .type(NotificationType.LIKE_COMMENT)
                    .build());

            this.webSocketNotificationService.notifyNewNotification(notificationResponse, userId);
        }

        return AssistantHelper.toMessageResponse("Comment liked successfully");
    }
}
