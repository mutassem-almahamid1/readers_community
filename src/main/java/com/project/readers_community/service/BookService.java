package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Book save(Book book);

    BookResponse create(BookRequest request);

    BookResponse getById(String id);

    BookResponse getByTitle(String name);

    List<BookResponse> getByAll();

    List<BookResponse> getByAllByIdIn(List<String> ids);

    Page<BookResponse> getByAllPage(int page, int size);

    MessageResponse update(String id, BookRequest request);

    MessageResponse softDeleteById(String id);

    MessageResponse softDeleteByCategoryId(String id);

    MessageResponse hardDeleteById(String id);

    List<BookResponse> getByCategory(String category);

    List<Book> searchBooksByCategory(String category);

    List<BookResponse> getTopRatedBooks();
    List<BookResponse> getPersonalizedRecommendations(String userId);
    List<BookResponse> getTrendingBooksThisMonth();
    List<BookResponse> getFriendRecommendations(String userId);

    MessageResponse updateBookReviewAndRating(String bookId, int reviewCount, double ratingTotal);

}
