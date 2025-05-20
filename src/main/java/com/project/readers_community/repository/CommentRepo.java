package com.project.readers_community.repository;

import com.project.readers_community.model.document.Comment;
import com.project.readers_community.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CommentRepo {
    Comment save(Comment comment);

    List<Comment> saveAll(List<Comment> comments);

    Comment getById(String id);

    Optional<Comment> getByIdAndStatusIfPresent(String id, Status status);

    Comment getByIdAndStatus(String id, Status status);

    List<Comment> getByReviewId(String reviewId);

    Page<Comment> getByReviewIdPaged(String reviewId, PageRequest pageRequest);

//    List<Comment> getByPostId(String postId);
//
//    Page<Comment> getByPostIdPaged(String postId, PageRequest pageRequest);

    List<Comment> getByUserId(String userId);

    List<Comment> getAllByIdIn(List<String> ids);

    void deleteById(String id);
}