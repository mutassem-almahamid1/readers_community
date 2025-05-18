package com.project.readers_community.repository;

import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface BookRepo {
    Book save(Book book);

    List<Book> saveAll(List<Book> books);

    Optional<Book> getByIdIfPresent(String id);

    Optional<Book> getByTitleIfPresent(String name);

    Book getById(String id);

    List<Book> getAllByIdIn(List<String> ids);


    Book getByTitle(String name);

    List<Book> getAll();

    Page<Book> getAllPage(PageRequest pageRequest);

    void deleteById(String id);

    void delete(Book book);

    List<Book> getAllByCategory(String category);
    
    List<Book> getAllIdIn(List<String> ids);

    List<Book> findTopBooksByRatingAndReviews(int limit);

    List<Book> findTrendingBooksForCurrentMonth(int limit);

    List<Book> findTopBooksByCategories(List<String> categoryIds, int limit);
}
