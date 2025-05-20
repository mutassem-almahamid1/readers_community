package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.request.UpdateReviewRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.service.*;
import com.project.readers_community.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Override
    public ReviewResponse create(ReviewRequest request, String userId) {
        Review review = ReviewMapper.mapToDocument(request, userId);
        UserResponse user = this.userService.getById(userId);
        bookService.getById(review.getBook());
        Review savedReview = reviewRepo.save(review);
        updateBookReviewStats(review.getBook());
        return ReviewMapper.mapToResponse(savedReview);
    }

    @Override
    public ReviewResponse getById(String id) {
        Review review = reviewRepo.getById(id);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(List.of(review.getBook()));
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(List.of(review.getUser()));
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId()));
    }

    @Override
    public List<ReviewResponse> getAll() {
        List<Review> reviews = reviewRepo.getAll();
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(reviews.stream().map(Review::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return reviews.stream()
                .map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.getAllPage(pageRequest);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(reviews.stream().map(Review::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return reviews.map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId())));
    }

    @Override
    public List<ReviewResponse> getByBookId(String bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(reviews.stream().map(Review::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return reviews.stream()
                .map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId())))
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByBookIdPage(String bookId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByBookIdPage(bookId, pageRequest);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(reviews.stream().map(Review::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));
        return reviews.map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId())));
    }

    @Override
    public List<ReviewResponse> getByUserId(String userId) {
        List<Review> reviews = reviewRepo.findByUserId(userId);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));
        return reviews.stream()
                .map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, this.commentService.getByReviewId(review.getId())))
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByUserIdPage(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByUserIdPage(userId, pageRequest);
        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));
        return reviews.map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, this.commentService.getByReviewId(review.getId())));
    }

    @Override
    public List<ReviewResponse> getByAllByIdIn(List<String> ids) {
        List<Review> reviews = reviewRepo.getAllByIdIn(ids);
        return reviews.stream()
                .map(ReviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewResponse> timeline(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<String> userFollowing = this.userService.getById(userId).getFollowing();

        // get all user mot follow random and get review
        List<String> userNotFollowing = this.userService.getAllByIdNotIn(userFollowing);
        int min = Math.min(userNotFollowing.size(), size); // In case the list has fewer than 10
        Collections.shuffle(userNotFollowing);
        List<String> random10Users = userNotFollowing.subList(0, min);

        userFollowing.addAll(random10Users);
        List<String> allUser = AssistantHelper.trimListAndUnique(userFollowing);

        Page<Review> reviews = reviewRepo.findAllByUserInAndStatus(allUser, pageRequest);

        List<BookResponse> bookResponses = this.bookService.getByAllByIdIn(reviews.stream().map(Review::getBook).toList());
        Map<String, BookResponse> bookResponseMap = bookResponses.stream()
                .collect(Collectors.toMap(BookResponse::getId, Function.identity()));

        List<UserResponse> userResponses = this.userService.getAllByIdIn(reviews.stream().map(Review::getUser).toList());
        Map<String, UserResponse> userResponseMap = userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        return reviews.map(review -> ReviewMapper.mapToResponse(review, bookResponseMap, userResponseMap, this.commentService.getByReviewId(review.getId())));
    }

    @Override
    public ReviewResponse likeReview(String id, String userId) {
        Review review = reviewRepo.getById(id);

        UserResponse user = userService.getById(userId);

        boolean liked = true;
        if (review.getLikedBy().contains(userId)) {
            liked = false;
            review.getLikedBy().remove(userId);
        } else {
            review.getLikedBy().add(userId);
        }

        Review updatedReview = reviewRepo.save(review);


        ///  notification for review user and save notfiy in table///
        if (!review.getUser().equals(userId) && liked) {
            NotificationResponse notificationResponse = notificationService.create(NotificationRequest.builder()
                    .recipientId(review.getUser())
                    .triggerUserId(userId)
                    .message(user.getUsername() + " liked your review.")
                    .reviewId(review.getId())
                    .commentId(review.getBook())
                    .type(NotificationType.LIKE_REVIEW)
                    .build());

            this.webSocketNotificationService.notifyNewNotification(notificationResponse, userId);
        }

        return ReviewMapper.mapToResponse(updatedReview);
    }

    @Override
    public MessageResponse update(String id, UpdateReviewRequest request, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().equals(userId)) {
            throw new BadReqException("You can only update your own reviews");
        }

        ReviewMapper.updateDocument(review, request);
        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());

        return AssistantHelper.toMessageResponse("Review Updated successfully");
    }

    @Override
    public MessageResponse softDeleteById(String id, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().equals(userId)) {
            throw new BadReqException("You can only delete your own reviews");
        }

        review.setStatus(Status.DELETED);
        review.setDeletedAt(LocalDateTime.now());

        reviewRepo.save(review);
        this.commentService.softDeleteByReviewId(review.getId());

        updateBookReviewStats(review.getBook());

        return AssistantHelper.toMessageResponse("Review deleted successfully");
    }

    @Override
    public MessageResponse softDeleteByBookId(String bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        reviews.forEach(review -> {
            review.setStatus(Status.DELETED);
            review.setDeletedAt(LocalDateTime.now());
            this.commentService.softDeleteByReviewId(review.getId());
        });
        reviewRepo.saveAll(reviews);
        return AssistantHelper.toMessageResponse("Review deleted successfully");
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().equals(userId)) {
            throw new BadReqException("You can only delete your own reviews");
        }

        reviewRepo.deleteById(id);

        updateBookReviewStats(review.getBook());

        return AssistantHelper.toMessageResponse("Review deleted successfully");
    }

    private void updateBookReviewStats(String bookId) {
        List<Review> activeReviews = reviewRepo.findByBookId(bookId)
                .stream()
                .toList();
        this.bookService.updateBookReviewAndRating(bookId, activeReviews.size(),
                activeReviews.stream().mapToDouble(Review::getRating).sum());
    }
}