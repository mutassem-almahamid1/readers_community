package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Comment;
import com.project.readers_community.model.document.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepoMongo extends MongoRepository<Comment, String> {
    Optional<Comment> findByIdAndStatus(String id, Status status);
    List<Comment> findAllByReviewIdAndStatus(String reviewId, Status status);
    Page<Comment> findAllByReviewIdAndStatus(String reviewId,Status status, PageRequest pageRequest);
    List<Comment> findAllByPostIdAndStatus(String postId, Status status);
    Page<Comment> findAllByPostIdAndStatus(String postId,Status status, PageRequest pageRequest);
    List<Comment> findAllByUserIdAndStatus(String userId, Status status);
}