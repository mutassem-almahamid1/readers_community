package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.ForbiddenException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.dto.request.UpdateReviewRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
import com.project.readers_community.service.NotificationService;
import com.project.readers_community.mapper.ReviewMapper;
import com.project.readers_community.service.ReviewService;
import com.project.readers_community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private PostServiceImpl postServiceImpl;
    @Autowired
    private NotificationService notificationService;

    @Override
    public ReviewResponse create(ReviewRequest request, String userId) {
        Review review = ReviewMapper.mapToDocument(request, userId);
        this.userService.getById(userId);
        bookService.getById(review.getBook());
        Review savedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());
//        PostRequest postRequest = new PostRequest(review.getId());
//        postServiceImpl.create(postRequest, userId);

        // إنشاء إشعار إذا كان المستخدم مختلفًا عن صاحب الكتاب
        ///  notification for added by and save notfiy in table///
//        if (!book.getAddedBy().equals(userId)) {
//            String message = user.getUsername() + "Reviewed your book.";
//            String bookId = review.getBook() ;
//            notificationService.createNotificationAsync(
//                    review.getUser(),
//                    userId,
//                    NotificationType.REVIEW_ON_BOOK,
//                    message,
//                    review.getId(),
//                    null,
//                    bookId,
//                    null
//            );
//        }

        return ReviewMapper.mapToResponse(savedReview);
    }

    @Override
    public ReviewResponse getById(String id) {
        Review review = reviewRepo.getById(id);
        return ReviewMapper.mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getAll() {
        List<Review> reviews = reviewRepo.getAll();
        return reviews.stream()
                .map(ReviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.getAllPage(pageRequest);
        return reviews.map(ReviewMapper::mapToResponse);
    }

    @Override
    public List<ReviewResponse> getByBookId(String bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        return reviews.stream()
                .map(ReviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByBookIdPage(String bookId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByBookIdPage(bookId, pageRequest);
        return reviews.map(ReviewMapper::mapToResponse);
    }

    @Override
    public List<ReviewResponse> getByUserId(String userId) {
        List<Review> reviews = reviewRepo.findByUserId(userId);
        return reviews.stream()
                .map(ReviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByUserIdPage(String userId, int page, int size) {
      PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByUserIdPage(userId, pageRequest);
        return reviews.map(ReviewMapper::mapToResponse);
    }

    @Override
    public ReviewResponse likeReview(String id, String userId) {
        Review review = reviewRepo.getById(id);

        UserResponse user = userService.getById(userId);

        if (review.getLikedBy().contains(userId)) {
            review.getLikedBy().remove(userId);
        } else {
            review.getLikedBy().add(userId);
        }

        Review updatedReview = reviewRepo.save(review);


        ///  notification for review user and save notfiy in table///
//        if (!review.getUser().equals(userId)) {
//            String message = user.getUsername() + " liked your review.";
//            String bookId = review.getBook() ;
//            notificationService.createNotificationAsync(
//                    review.getUser(),
//                    userId,
//                    NotificationType.LIKE_REVIEW,
//                    message,
//                    review.getId(),
//                    null,
//                    bookId,
//                    null
//            );
//        }

        return ReviewMapper.mapToResponse(updatedReview);
    }

    @Override
    public ReviewResponse update(String id, UpdateReviewRequest request, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().equals(userId)) {
            throw new BadReqException("You can only update your own reviews");
        }

        ReviewMapper.updateDocument(review, request);
        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());

        return ReviewMapper.mapToResponse(updatedReview);
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

        updateBookReviewStats(review.getBook());

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