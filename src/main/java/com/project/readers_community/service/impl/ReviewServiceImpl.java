package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.ForbiddenException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Comment;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.CommentRequest;
import com.project.readers_community.model.dto.request.PostRequest;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.CommentResponse;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.model.document.NotificationType;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.NotificationService;
import com.project.readers_community.mapper.ReviewMapper;
import com.project.readers_community.service.ReviewService;
import lombok.RequiredArgsConstructor;
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
    private BookRepo bookRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostServiceImpl postServiceImpl;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public ReviewResponse create(ReviewRequest request, String userId) {
        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        Book book = bookRepo.getById(request.getBookId());
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        Review review = reviewMapper.mapToDocument(request, userId);
        Review savedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());
        PostRequest postRequest = new PostRequest(review.getId());
        postServiceImpl.create(postRequest, userId);

        // إنشاء إشعار إذا كان المستخدم مختلفًا عن صاحب الكتاب
        if (!book.getAddedBy().equals(userId)) {
            String message = user.getUsername() + "Reviewed your book.";
            String bookId = review.getBook() ;
            notificationService.createNotificationAsync(
                    review.getUser(),
                    userId,
                    NotificationType.REVIEW_ON_BOOK,
                    message,
                    review.getId(),
                    null,
                    bookId,
                    null
            );
        }


        return reviewMapper.mapToResponse(savedReview, userId);
    }

    @Override
    public ReviewResponse getById(String id) {
        Review review = reviewRepo.getById(id);
        if (review == null || review.getStatus() != Status.ACTIVE) {
            throw new NotFoundException("Review not found");
        }
        return reviewMapper.mapToResponse(review, null);
    }

    @Override
    public List<ReviewResponse> getAll() {
        List<Review> reviews = reviewRepo.getAll();
        return reviews.stream()
                .filter(review -> review.getStatus() == Status.ACTIVE)
                .map(review -> reviewMapper.mapToResponse(review, null))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.getAllPage(pageRequest);
        return reviews.map(review -> {
            if (review.getStatus() == Status.ACTIVE) {
                return reviewMapper.mapToResponse(review, null);
            }
            return null;
        });
    }

    @Override
    public List<ReviewResponse> getByBookId(String bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        return reviews.stream()
                .filter(review -> review.getStatus() == Status.ACTIVE)
                .map(review -> reviewMapper.mapToResponse(review, null))
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByBookIdPage(String bookId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByBookIdPage(bookId, pageRequest);
        return reviews.map(review -> {
            if (review.getStatus() == Status.ACTIVE) {
                return reviewMapper.mapToResponse(review, null);
            }
            return null;
        });
    }

    @Override
    public List<ReviewResponse> getByUserId(String userId) {
        List<Review> reviews = reviewRepo.findByUserId(userId);
        return reviews.stream()
                .filter(review -> review.getStatus() == Status.ACTIVE)
                .map(review -> reviewMapper.mapToResponse(review, userId))
                .collect(Collectors.toList());
    }


    @Override
    public Page<ReviewResponse> getByUserIdPage(String userId, int page, int size) {
      PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.findByUserIdPage(userId, pageRequest);
        return reviews.map(review -> {
            if (review.getStatus() == Status.ACTIVE) {
                return reviewMapper.mapToResponse(review, userId);
            }
            return null;
        });
    }

    @Override
    public ReviewResponse likeReview(String id, String userId) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        User user = userRepo.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        review.getLikedBy().add(userId);
        Review updatedReview = reviewRepo.save(review);

        if (!review.getUser().equals(userId)) {
            String message = user.getUsername() + " liked your review.";
            String bookId = review.getBook() ;
            notificationService.createNotificationAsync(
                    review.getUser(),
                    userId,
                    NotificationType.LIKE_REVIEW,
                    message,
                    review.getId(),
                    null,
                    bookId,
                    null
            );
        }

        return reviewMapper.mapToResponse(updatedReview, userId);
    }



    @Override
    public ReviewResponse update(String id, ReviewRequest request, String userId) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().equals(userId)) {
            throw new ForbiddenException("You can only update your own reviews");
        }

        Book book = bookRepo.getById(request.getBookId());
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        reviewMapper.updateDocument(review, request);
        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());

        return reviewMapper.mapToResponse(updatedReview, userId);
    }

    @Override
    public ReviewResponse softDeleteById(String id, String userId) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own reviews");
        }

        review.setStatus(Status.DELETED);
        review.setDeletedAt(LocalDateTime.now());

        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());

        return reviewMapper.mapToResponse(updatedReview, userId);
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Review review = reviewRepo.getByIdIfPresent(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().equals(userId)) {
            throw new ForbiddenException("You can only delete your own reviews");
        }

        reviewRepo.deleteById(id);

        updateBookReviewStats(review.getBook());

        return MessageResponse.builder().message("Review deleted successfully").build();
    }

    private void updateBookReviewStats(String bookId) {
        List<Review> activeReviews = reviewRepo.findByBookId(bookId)
                .stream()
                .filter(review -> review.getStatus() == Status.ACTIVE)
                .collect(Collectors.toList());

        int reviewCount = activeReviews.size();
        Book book = bookRepo.getById(bookId);
        book.setReviewCount(reviewCount);

        double avgRating = reviewCount > 0
                ? activeReviews.stream().mapToDouble(Review::getRating).average().orElse(0.0)
                : 0.0;
        book.setAvgRating(avgRating);

        bookRepo.save(book);
    }
}