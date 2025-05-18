package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.mongo.BookRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepoImpl implements BookRepo {
    @Autowired
    private BookRepoMongo repoMongo;

    @Override
    public Book save(Book book) {
        return repoMongo.save(book);
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        return repoMongo.saveAll(books);
    }

    @Override
    public Optional<Book> getByIdIfPresent(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<Book> getByTitleIfPresent(String name) {
        return repoMongo.findByTitleAndStatus(name, Status.ACTIVE);
    }

    @Override
    public Book getById(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public Book getByTitle(String name) {
        return repoMongo.findByTitleAndStatus(name, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public List<Book> getAll() {
        return repoMongo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Page<Book> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatus(Status.ACTIVE, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(Book book) {
        repoMongo.delete(book);
    }

    @Override
    public List<Book> getAllByCategory(String category) {
         return repoMongo.findAllByCategory(category);
    }

    @Override
    public List<Book> getAllIdIn(List<String> ids) {
        return repoMongo.findAllByIdInAndStatus(ids, Status.ACTIVE);
    }

    @Override
    public List<Book> findTopBooksByRatingAndReviews(int limit) {
        List<Book> books = repoMongo.findTopByStatusOrderByAvgRatingDescReviewCountDesc(Status.ACTIVE);
        return books.size() > limit ? books.subList(0, limit) : books;
    }

    @Override
    public List<Book> findTrendingBooksForCurrentMonth(int limit) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Book> books = repoMongo.findByStatusAndCreatedAtAfterOrderByReaderCountDesc(Status.ACTIVE, startOfMonth);
        return books.size() > limit ? books.subList(0, limit) : books;
    }

    @Override
    public List<Book> findTopBooksByCategories(List<String> categoryIds, int limit) {
        List<Book> books = repoMongo.findTopByStatusAndCategoryInOrderByAvgRatingDescReviewCountDesc(Status.ACTIVE, categoryIds);
        return books.size() > limit ? books.subList(0, limit) : books;
    }
}
