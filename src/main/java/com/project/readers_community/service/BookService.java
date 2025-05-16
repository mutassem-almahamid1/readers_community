package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Book save(Book book);

    BookResponse create(BookRequest request, String addedById);

    BookResponse getById(String id);

    BookResponse getByTitle(String name);

    List<BookResponse> getByAll();

    Page<BookResponse> getByAllPage(int page, int size);

    MessageResponse update(String id, BookRequest request);

    MessageResponse softDeleteById(String id);

    MessageResponse hardDeleteById(String id);

    List<BookResponse> getByCategory(String category);

    List<Book> searchBooksByCategory(String category);

    List<BookResponse> getBookSuggestions(int limit);

    List<BookResponse> getTrendingBooks(int limit);

    List<BookResponse> getPersonalizedBookSuggestions(String userId, int limit);

    MessageResponse updateBookReviewAndRating(String bookId, int reviewCount, double ratingTotal);

}
