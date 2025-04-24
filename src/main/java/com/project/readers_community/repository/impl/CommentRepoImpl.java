package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Comment;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.repository.CommentRepo;
import com.project.readers_community.repository.mongo.CommentRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepoImpl implements CommentRepo {
    @Autowired
    private CommentRepoMongo commentRepoMongo;

    @Override
    public Comment save(Comment comment) {
        return commentRepoMongo.save(comment);
    }

    @Override
    public Optional<Comment> getById(String id) {
        return commentRepoMongo.findById(id);
    }

    @Override
    public Optional<Comment> getByIdAndStatus(String id, Status status) {
        return commentRepoMongo.findByIdAndStatus(id, status);
    }

    @Override
    public List<Comment> getByReviewId(String reviewId) {
        return commentRepoMongo.findAllByReviewIdAndStatus(reviewId, Status.ACTIVE);
    }

    @Override
    public Page<Comment> getByReviewIdPaged(String reviewId, PageRequest pageRequest) {
        return commentRepoMongo.findAllByReviewIdAndStatus(reviewId, Status.ACTIVE, pageRequest);
    }

    @Override
    public List<Comment> getByPostId(String postId) {
      return commentRepoMongo.findAllByPostIdAndStatus(postId, Status.ACTIVE);
    }

    @Override
    public Page<Comment> getByPostIdPaged(String postId, PageRequest pageRequest) {
        return commentRepoMongo.findAllByPostIdAndStatus(postId, Status.ACTIVE, pageRequest);
    }

    @Override
    public List<Comment> getByUserId(String userId) {
        return commentRepoMongo.findAllByUserIdAndStatus(userId, Status.ACTIVE);
    }

    @Override
    public void deleteById(String id) {
        if (!commentRepoMongo.existsById(id)) {
            throw new NotFoundException("Comment not found");
        }
        commentRepoMongo.deleteById(id);
    }
}