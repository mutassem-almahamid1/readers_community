package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepoMongo extends MongoRepository<Post, String> {
    Optional<Post> findByIdAndStatus(String id, Status status);
    List<Post> findAllByStatus(Status status);
    Page<Post> findAllByStatus(Status status, PageRequest pageRequest);
    List<Post> findAllByUserIdAndStatus(String userId, Status status);
    List<Post> findAllByReviewIdAndStatus(String reviewId, Status status);
}