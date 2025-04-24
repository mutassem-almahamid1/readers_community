package com.project.readers_community.repository;

import com.project.readers_community.model.document.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface PostRepo {


    Post save(Post post);

    Optional<Post> getByIdIfPresent(String id);

    Post getById(String id);

    List<Post> getAll();

    Page<Post> getAllPage(PageRequest pageRequest);

    Page<Post> getAllPageByUser(String id, PageRequest pageRequest);

    Page<Post> getAllPageByReview(String id, PageRequest pageRequest);

    void deleteById(String id);

    void delete(Post post);

    List<Post> findByReviewId(String reviewId);

    List<Post> findByUserId(String userId);

}

