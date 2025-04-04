package com.project.readers_community.repository;

import com.project.readers_community.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface BookRepository extends MongoRepository<Book, String> {
    Book findByIsbn(String isbn);
    Book findByTitle(String title);  // دالة للبحث حسب العنوان

    @Query("{ 'categories': { $in: ?0 } }")
    List<Book> findByCategoriesIn(Set<String> categories);

    @Query("{ 'title': { $in: ?0 } }")
    List<Book> findByTitleIn(Set<String> titles);
}