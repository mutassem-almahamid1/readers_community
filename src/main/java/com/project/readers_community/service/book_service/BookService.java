package com.project.readers_community.service.book_service;

import com.project.readers_community.entity.Book;
import com.project.readers_community.dto.book_dto.BookDTORequest;
import java.util.List;
import java.util.Map;

public interface BookService {
    List<Book> fetchBookFromGoogle(String query, String addedBy);
    void deleteBookById(String bookId);
    long deleteAllBooks();
    List<Book> getAllBooks();
    Book findBookByIsbn(String isbn);
    Book findBookById(String id);
    Book findBookByTitle(String title);
    Book saveBookFromDTO(BookDTORequest bookDTO);
    Book updateBookByTitle(String title, Map<String, Object> updates);
}
