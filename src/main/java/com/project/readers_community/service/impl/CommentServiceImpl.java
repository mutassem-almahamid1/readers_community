package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.ForbiddenException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.UpdateCommentRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.repository.CommentRepo;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.CommentService;
import com.project.readers_community.service.NotificationService;
import com.project.readers_community.mapper.CommentMapper;
import com.project.readers_community.service.ReviewService;
import com.project.readers_community.service.UserService;
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
    private ReviewService reviewService;
//    @Autowired
//    private PostRepo postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public CommentResponse createCommentOnReview(CommentRequest request, String userId) {
        Comment comment = CommentMapper.mapToDocument(request, userId);
        userService.getById(userId);
        reviewService.getById(request.getReviewId());
        Comment savedComment = commentRepo.save(comment);

//        if (!review.getUser().equals(userId)) {
//            String message = user.getUsername() + " commented on your review.";
//            String bookId = review.getBook();
//            notificationService.createNotificationAsync(
//                    review.getUser(),
//                    userId,
//                    NotificationType.COMMENT_ON_REVIEW,
//                    message,
//                    review.getId(),
//                    savedComment.getId(),
//                    bookId,
//                    null
//            );
//        }

        return CommentMapper.mapToResponse(savedComment);
    }

//    @Override
//    public CommentResponse createCommentOnPost(CommentRequest request, String userId) {
//        User user = userRepo.getById(userId);
//        if (user == null) {
//            throw new NotFoundException("User not found");
//        }
//
//        Post post = postRepo.getById(request.getPostId());
//        if (post == null) {
//            throw new NotFoundException("Post not found");
//        }
//
//        Comment comment = commentMapper.mapToDocument(request, userId);
//        Comment savedComment = commentRepo.save(comment);
//
//        if (!post.getUser().equals(userId)) {
//            String message = user.getUsername() + " commented on your post.";
//            notificationService.createNotificationAsync(
//                    post.getUser(),
//                    userId,
//                    NotificationType.COMMENT_ON_POST,
//                    message,
//                    post.getId(),
//                    savedComment.getId(),
//                    null,
//                    null
//            );
//        }
//
//        return commentMapper.mapToResponse(savedComment, userId);
//    }

    @Override
    public CommentResponse getCommentById(String id) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        return CommentMapper.mapToResponse(comment);
    }

    @Override
    public List<CommentResponse> getByReviewId(String reviewId) {
        List<Comment> comments = commentRepo.getByReviewId(reviewId);
        return comments.stream()
                .map(CommentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentResponse> getByReviewIdPaged(String reviewId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepo.getByReviewIdPaged(reviewId, pageRequest);
        return comments.map(CommentMapper::mapToResponse);
    }

//    @Override
//    public List<CommentResponse> getCommentsByPostId(String postId) {
//        List<Comment> comments = commentRepo.getByPostId(postId);
//        return comments.stream()
//               .map(comment -> commentMapper.mapToResponse(comment, null))
//               .collect(Collectors.toList());
//    }
//
//    @Override
//    public Page<CommentResponse> getCommentsByPostIdPaged(String postId, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        Page<Comment> comments = commentRepo.getByPostIdPaged(postId, pageRequest);
//        return comments.map(comment ->
//                commentMapper.mapToResponse(comment, null));
//    }

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
    public MessageResponse hardDeleteById(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        if (!comment.getUser().equals(userId)) {
            throw new BadReqException("You can only delete your own comments");
        }
        commentRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Comment deleted successfully");
    }

    @Override
    public CommentResponse likeComment(String id, String userId) {
        Comment comment = commentRepo.getByIdAndStatus(id, Status.ACTIVE);
        userService.getById(userId);
        
        if (comment.getLikedBy() == null) {
            comment.setLikedBy(new HashSet<>());
        }

        if (comment.getLikedBy().contains(userId)){
            comment.getLikedBy().remove(userId);
        } else {
            comment.getLikedBy().add(userId);
        }

        comment.setLikeCount(comment.getLikedBy().size());
        Comment updatedComment = commentRepo.save(comment);


//        if (!comment.getUser().equals(userId)) {
//            String message = user.getUsername() + " liked your comment.";
//            String reviewId = comment.getReview();
//            String postId = comment.getPost();
//            notificationService.createNotificationAsync(
//                    comment.getUser(),
//                    userId,
//                    NotificationType.LIKE_COMMENT,
//                    message,
//                    reviewId,
//                    comment.getId(),
//                    null,
//                    postId
//            );
//        }

        return CommentMapper.mapToResponse(updatedComment);
    }
}
