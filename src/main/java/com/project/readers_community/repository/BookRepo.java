package com.project.readers_community.repository;

import com.project.readers_community.model.document.Book;
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
    Book getByName(String name);
    List<Book> getAll();
    Page<Book> getAllPage(PageRequest pageRequest);
    void deleteById(String id);
    void delete(Book book);
    Optional<Book> getAllByCategory(String category);
    List<Book> findTopBooksByRatingAndReviews(int limit);
    List<Book> findTrendingBooksForCurrentMonth(int limit);
    List<Book> findTopBooksByCategories(List<String> categoryIds, int limit);
}
