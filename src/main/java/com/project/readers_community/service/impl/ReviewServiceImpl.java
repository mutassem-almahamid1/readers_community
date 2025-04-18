package com.project.readers_community.service.impl;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.ReviewRequest;
import com.project.readers_community.model.dto.response.ReviewResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.mapper.ReviewMapper;
import com.project.readers_community.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    private ReviewMapper reviewMapper;

    @Override
    public ReviewResponse create(ReviewRequest request, String userId) {
        User user = userRepo.getById(userId);

        Book book = bookRepo.getById(request.getBookId());

        Review review = reviewMapper.mapToDocument(request, user, book);

        Review savedReview = reviewRepo.save(review);

        updateBookReviewStats(book);

        return reviewMapper.mapToResponse(savedReview);
    }

    @Override
    public ReviewResponse getById(String id) {
        Review review = reviewRepo.getById(id);
        return reviewMapper.mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getAll() {
        List<Review> reviews = reviewRepo.getAll();
        return reviews.stream()
                .map(reviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewResponse> getAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepo.getAllPage(pageRequest);
        return reviews.map(reviewMapper::mapToResponse);
    }

    @Override
    public List<ReviewResponse> getByBookId(String bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        return reviews.stream()
                .map(reviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getByUserId(String userId) {
        List<Review> reviews = reviewRepo.findByUserId(userId);
        return reviews.stream()
                .map(reviewMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse update(String id, ReviewRequest request, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own reviews");
        }

        Book book = bookRepo.getById(request.getBookId());

        reviewMapper.updateDocument(review, request, book);

        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(book);

        return reviewMapper.mapToResponse(updatedReview);
    }

    @Override
    public ReviewResponse softDeleteById(String id, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        review.setStatus(Status.DELETED);
        review.setDeletedAt(java.time.LocalDateTime.now());

        Review updatedReview = reviewRepo.save(review);

        updateBookReviewStats(review.getBook());

        return reviewMapper.mapToResponse(updatedReview);
    }

    @Override
    public MessageResponse hardDeleteById(String id, String userId) {
        Review review = reviewRepo.getById(id);

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepo.deleteById(id);

        updateBookReviewStats(review.getBook());

        return MessageResponse.builder().message("Review deleted successfully").build();
    }

    private void updateBookReviewStats(Book book) {
        List<Review> activeReviews = reviewRepo.findByBookId(book.getId());

        int reviewCount = activeReviews.size();
        book.setReviewCount(reviewCount);

        double avgRating = reviewCount > 0
                ? activeReviews.stream().mapToInt(Review::getRating).average().orElse(0.0)
                : 0.0;
        book.setAvgRating(avgRating);

        bookRepo.save(book);
    }
}