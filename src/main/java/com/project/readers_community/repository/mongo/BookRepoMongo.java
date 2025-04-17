package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepoMongo extends MongoRepository<Book, String> {
    Optional<Book> findByIdAndStatus(String id, Status status);
    List<Book> findAllByStatus(Status status);
    Page<Book> findAllByStatus(Status status, PageRequest pageRequest);
    Optional<Book> findAllByCategory(String category);
}