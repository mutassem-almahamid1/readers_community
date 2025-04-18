package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Review;
import com.project.readers_community.model.document.Status;
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
    List<Review> findAllByBookIdAndStatus(String bookId, Status status);
    List<Review> findAllByUserIdAndStatus(String userId, Status status);

}
