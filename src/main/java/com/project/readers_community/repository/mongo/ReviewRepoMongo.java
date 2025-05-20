package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepoMongo extends MongoRepository<Review, String> {
    Optional<Review> findByIdAndStatus(String id, Status status);

    List<Review> findAllByStatus(Status status);

    Page<Review> findAllByStatus(Status status, PageRequest pageRequest);

    List<Review> findAllByBookAndStatus(String book, Status status);

    Page<Review> findAllByBookAndStatus(String book, Status status, PageRequest pageRequest);

    List<Review> findAllByUserAndStatus(String user, Status status);

    Page<Review> findAllByUserAndStatus(String user, Status status, PageRequest pageRequest);

    Page<Review> findAllByUserInAndStatus(List<String> users, Status status, PageRequest pageRequest);

    List<Review> findAllByIdInAndStatus(List<String> ids, Status status);
}
