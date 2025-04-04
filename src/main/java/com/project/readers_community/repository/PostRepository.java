package com.project.readers_community.repository;

import com.project.readers_community.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByGroupId(String groupId);
}