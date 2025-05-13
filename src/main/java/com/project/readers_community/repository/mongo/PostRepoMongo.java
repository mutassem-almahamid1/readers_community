package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.enums.Status;
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
    List<Post> findAllByUserAndStatus(String user, Status status);
    Page<Post> findAllByUserAndStatus(String user, Status status, PageRequest pageRequest);
    List<Post> findAllByReviewAndStatus(String review, Status status);
    Page<Post> findAllByReviewAndStatus(String review, Status status, PageRequest pageRequest);
}
