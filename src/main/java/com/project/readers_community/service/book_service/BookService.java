package com.project.readers_community.service.book_service;

import com.project.readers_community.entity.Book;
import java.util.List;

public interface BookService {
    List<Book> fetchBookFromGoogle(String query, String addedBy);
    void deleteBookById(String bookId);
    long deleteAllBooks();
    List<Book> getAllBooks();
    Book findBookByIsbn(String isbn);
    Book findBookById(String id);
    Book findBookByTitle(String title);
    Book saveBook(Book book);

}
