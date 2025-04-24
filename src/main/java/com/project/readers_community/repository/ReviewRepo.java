package com.project.readers_community.repository;

import com.project.readers_community.model.document.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ReviewRepo {

    Review save(Review review);

    Optional<Review> getByIdIfPresent(String id);

    Review getById(String id);

    List<Review> getAll();

    Page<Review> getAllPage(PageRequest pageRequest);

    void deleteById(String id);

    void delete(Review review);

    List<Review> findByBookId(String bookId);

    Page<Review> findByBookIdPage(String bookId, PageRequest pageRequest);

    List<Review> findByUserId(String userId);

    Page<Review> findByUserIdPage(String userId, PageRequest pageRequest);
}
