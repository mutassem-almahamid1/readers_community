package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepoMongo extends MongoRepository<Category, String> {
    Optional<Category> findByIdAndStatus(String id, Status status);

    Optional<Category> findByNameAndStatus(String name, Status status);

    List<Category> findAllByStatus(Status status);

    Page<Category> findAllByStatus(Status status, PageRequest pageRequest);
}
