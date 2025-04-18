package com.project.readers_community.repository;

import com.project.readers_community.model.document.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ReviewRepo {

    Review save(Review review);

    List<Review> saveAll(List<Review> reviews);

    Optional<Review> getByIdIfPresent(String id);

    Review getById(String id);

    List<Review> getAll();

    Page<Review> getAllPage(PageRequest pageRequest);

    void deleteById(String id);

    void delete(Review review);

    List<Review> findByBookId(String bookId);

    List<Review> findByUserId(String userId);


}
