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
    List<Comment> findAllByReviewAndStatus(String review, Status status);
    Page<Comment> findAllByReviewAndStatus(String review, Status status, PageRequest pageRequest);
    List<Comment> findAllByPostAndStatus(String post, Status status);
    Page<Comment> findAllByPostAndStatus(String post, Status status, PageRequest pageRequest);
    List<Comment> findAllByUserAndStatus(String user, Status status);
}
