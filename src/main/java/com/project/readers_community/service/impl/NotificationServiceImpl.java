package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.*;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.CommentRepo;
import com.project.readers_community.repository.NotificationRepo;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.mapper.NotificationMapper;
import com.project.readers_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private NotificationMapper notificationMapper;


    @Async
    @Override
    public void createNotificationAsync(String recipientId, String triggerUserId,
                                        NotificationType type, String message,
                                        String reviewId, String commentId, String bookId, String postId) {
        // إنشاء كائن NotificationRequest بناءً على المعاملات
        NotificationRequest request = new NotificationRequest();
        request.setRecipientId(recipientId);
        request.setTriggerUserId(triggerUserId);
        request.setType(type);
        request.setMessage(message);
        request.setReviewId(reviewId);
        request.setCommentId(commentId);
        request.setBookId(bookId);
        request.setPostId(postId);

        // استدعاء دالة create لإنشاء الإشعار
        create(request);
    }

    @Override
    public NotificationResponse getById(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        return notificationMapper.mapToResponse(notification);
    }

    @Override
    public List<NotificationResponse> getByRecipientId(String recipientId) {
        List<Notification> notifications = notificationRepo.getByRecipientId(recipientId);
        return notifications.stream()
                .map(notificationMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponse> getByRecipientIdPaged(String recipientId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepo.getByRecipientId(recipientId, pageRequest);
        return notifications.map(notificationMapper::mapToResponse);
    }

    @Override
    public List<NotificationResponse> getUnreadByRecipientId(String recipientId) {
        List<Notification> notifications = notificationRepo.getUnreadByRecipientId(recipientId);
        return notifications.stream()
                .map(notificationMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponse update(String id, NotificationRequest request) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        User recipient = userRepo.getById(request.getRecipientId());
        if (recipient == null) {
            throw new NotFoundException("Recipient user not found");
        }
        User triggerUser = userRepo.getById(request.getTriggerUserId());
        if (triggerUser == null) {
            throw new NotFoundException("Trigger user not found");
        }
        Review review = request.getReviewId() != null ? reviewRepo.getById(request.getReviewId()) : null;
        Optional<Comment> comment = request.getCommentId() != null ? commentRepo.getById(request.getCommentId()) : null;
        Book book = request.getBookId() != null ? bookRepo.getById(request.getBookId()) : null;
        Post post = request.getPostId() != null ? postRepo.getById(request.getPostId()) : null;

        notificationMapper.updateDocument(notification, request, recipient, triggerUser, review, comment.orElse(null), book, post);
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }

    @Override
    public NotificationResponse softDeleteById(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true); // الحذف الناعم يعني وضع علامة مقروء
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        notificationRepo.deleteById(id);
        return MessageResponse.builder().message("Notification deleted successfully").build();
    }

    @Override
    public NotificationResponse markAsRead(String id) {
        Notification notification = notificationRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        Notification updatedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(updatedNotification);
    }

    @Override
    public NotificationResponse create(NotificationRequest request) {
        User recipient = userRepo.getById(request.getRecipientId());
        if (recipient == null) {
            throw new NotFoundException("Recipient user not found");
        }
        User triggerUser = userRepo.getById(request.getTriggerUserId());
        if (triggerUser == null) {
            throw new NotFoundException("Trigger user not found");
        }

        // جلب الكيانات المرتبطة إذا كانت المعرفات موجودة
        Review review = request.getReviewId() != null ? reviewRepo.getById(request.getReviewId()) : null;
        Optional<Comment> comment = request.getCommentId() != null ? commentRepo.getById(request.getCommentId()) : null;
        Book book = request.getBookId() != null ? bookRepo.getById(request.getBookId()) : null;
        Post post = request.getPostId() != null ? postRepo.getById(request.getPostId()) : null;

        // تمرير الكيانات إلى المابير
        Notification notification = notificationMapper.mapToDocument(request, recipient, triggerUser, review, comment.orElse(null), book, post);
        Notification savedNotification = notificationRepo.save(notification);
        return notificationMapper.mapToResponse(savedNotification);
    }






}