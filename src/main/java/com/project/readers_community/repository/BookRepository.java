package com.project.readers_community.repository;

import com.project.readers_community.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    boolean existsBy();
}
