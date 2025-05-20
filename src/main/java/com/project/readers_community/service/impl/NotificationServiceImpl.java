package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.*;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.repository.NotificationRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.mapper.NotificationMapper;
import com.project.readers_community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Override
    public NotificationResponse create(NotificationRequest request) {
        // Validate users exist
        userRepo.getById(request.getRecipientId());
        userRepo.getById(request.getTriggerUserId());

        Notification notification = NotificationMapper.mapToDocument(request);
        Notification savedNotification = notificationRepo.save(notification);

        List<UserResponse> userResponses = this.userService.getAllByIdIn(List.of(savedNotification.getTriggerUser()));
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(List.of(savedNotification.getBook()));
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<ReviewResponse> reviewResponses = this.reviewService.getByAllByIdIn(List.of(savedNotification.getReview()));
        Map<String, ReviewResponse> reviewResponseMap = reviewResponses.stream()
                .collect(Collectors.toMap(ReviewResponse::getId, Function.identity()));

        List<CommentResponse> commentResponses = this.commentService.getByAllByIdIn(List.of(savedNotification.getComment()));
        Map<String, CommentResponse> commentResponseMap = commentResponses.stream()
                .collect(Collectors.toMap(CommentResponse::getId, Function.identity()));

        return NotificationMapper.mapToResponse(notification, userResponseMap, bookResponseMap, reviewResponseMap, commentResponseMap);
    }

    @Override
    public NotificationResponse getById(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(List.of(notification.getTriggerUser()));
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(List.of(notification.getBook()));
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<ReviewResponse> reviewResponses = this.reviewService.getByAllByIdIn(List.of(notification.getReview()));
        Map<String, ReviewResponse> reviewResponseMap = reviewResponses.stream()
                .collect(Collectors.toMap(ReviewResponse::getId, Function.identity()));

        List<CommentResponse> commentResponses = this.commentService.getByAllByIdIn(List.of(notification.getComment()));
        Map<String, CommentResponse> commentResponseMap = commentResponses.stream()
                .collect(Collectors.toMap(CommentResponse::getId, Function.identity()));

        return NotificationMapper.mapToResponse(notification, userResponseMap, bookResponseMap, reviewResponseMap, commentResponseMap);
    }

    @Override
    public List<NotificationResponse> getByRecipientId(String recipientId, NotificationType type) {
        List<Notification> notifications = null;
        if (type != null){
            notifications = notificationRepo.getByRecipientIdAndType(recipientId, type);
        }else {
            notifications = notificationRepo.getByRecipientId(recipientId);
        }

        List<UserResponse> userResponses = this.userService.getAllByIdIn(notifications.stream().map(Notification::getTriggerUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(notifications.stream().map(Notification::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<ReviewResponse> reviewResponses = this.reviewService.getByAllByIdIn(notifications.stream().map(Notification::getReview).toList());
        Map<String, ReviewResponse> reviewResponseMap = reviewResponses.stream()
                .collect(Collectors.toMap(ReviewResponse::getId, Function.identity()));

        List<CommentResponse> commentResponses = this.commentService.getByAllByIdIn(notifications.stream().map(Notification::getComment).toList());
        Map<String, CommentResponse> commentResponseMap = commentResponses.stream()
                .collect(Collectors.toMap(CommentResponse::getId, Function.identity()));

        return notifications.stream()
                .map(notification -> NotificationMapper.mapToResponse(notification, userResponseMap, bookResponseMap, reviewResponseMap, commentResponseMap))
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponse> getByRecipientIdPaged(String recipientId, NotificationType type, int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("isRead")).and(Sort.by(Sort.Order.desc("createdAt")));
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Notification> notifications = null;

        if (type != null){
            notifications = notificationRepo.getByRecipientIdAndType(recipientId, type, pageRequest);
        } else {
            notifications = notificationRepo.getByRecipientId(recipientId, pageRequest);
        }

        List<UserResponse> userResponses = this.userService.getAllByIdIn(notifications.stream().map(Notification::getTriggerUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(notifications.stream().map(Notification::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<ReviewResponse> reviewResponses = this.reviewService.getByAllByIdIn(notifications.stream().map(Notification::getReview).toList());
        Map<String, ReviewResponse> reviewResponseMap = reviewResponses.stream()
                .collect(Collectors.toMap(ReviewResponse::getId, Function.identity()));

        List<CommentResponse> commentResponses = this.commentService.getByAllByIdIn(notifications.stream().map(Notification::getComment).toList());
        Map<String, CommentResponse> commentResponseMap = commentResponses.stream()
                .collect(Collectors.toMap(CommentResponse::getId, Function.identity()));

        return notifications.map(notification -> NotificationMapper.mapToResponse(notification, userResponseMap, bookResponseMap, reviewResponseMap, commentResponseMap));
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        notificationRepo.deleteById(id);
        return MessageResponse.builder().message("Notification deleted successfully").build();
    }

    @Override
    public MessageResponse markAsRead(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
        return AssistantHelper.toMessageResponse("Reading Successfully.");
    }

    @Override
    public MessageResponse markAsReadByRecipientId(String recipientId) {
        List<Notification> notifications = notificationRepo.getByRecipientId(recipientId);
        notifications.forEach(notification -> {
            notification.setRead(true);
        });
        this.notificationRepo.saveAll(notifications);

        return AssistantHelper.toMessageResponse("Reading All Successfully.");
    }
}
