package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.repository.ReviewRepo;
import com.project.readers_community.repository.mongo.ReviewRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepoImpl implements ReviewRepo {

    @Autowired
    private ReviewRepoMongo repoMongo;

    @Override
    public Review save(Review review) {
        return repoMongo.save(review);
    }

    @Override
    public List<Review> saveAll(List<Review> reviews) {
        return repoMongo.saveAll(reviews);
    }

    @Override
    public Optional<Review> getByIdIfPresent(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Review getById(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @Override
    public List<Review> getAll() {
        return repoMongo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Page<Review> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatus(Status.ACTIVE, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(Review review) {
        repoMongo.delete(review);
    }

    @Override
    public List<Review> findByBookId(String bookId) {
        return repoMongo.findAllByBookIdAndStatus(bookId, Status.ACTIVE);
    }

    @Override
    public List<Review> findByUserId(String userId) {
        return repoMongo.findAllByUserIdAndStatus(userId, Status.ACTIVE);
    }
}