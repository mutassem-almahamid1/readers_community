package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Post;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.repository.PostRepo;
import com.project.readers_community.repository.mongo.PostRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepoImpl implements PostRepo {
    @Autowired
    private PostRepoMongo repoMongo;

    @Override
    public Post save(Post post) {
        return repoMongo.save(post);
    }



    @Override
    public Optional<Post> getByIdIfPresent(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Post getById(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    @Override
    public List<Post> getAll() {
        return repoMongo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Page<Post> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatus(Status.ACTIVE, pageRequest);
    }

    @Override
    public Page<Post> getAllPageByUser(String id, PageRequest pageRequest){
        return repoMongo.findAllByReviewIdAndStatus(id,Status.ACTIVE, pageRequest);
    }

    @Override
    public Page<Post> getAllPageByReview(String id, PageRequest pageRequest){
        return repoMongo.findAllByReviewIdAndStatus(id , Status.ACTIVE,pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(Post post) {
        repoMongo.delete(post);
    }

    @Override
    public List<Post> findByUserId(String userId) {
        return repoMongo.findAllByUserIdAndStatus(userId, Status.ACTIVE);
    }

    @Override
    public List<Post> findByReviewId(String reviewId) {
        return repoMongo.findAllByReviewIdAndStatus(reviewId, Status.ACTIVE);
    }
}