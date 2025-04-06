package com.project.readers_community.controller;

import com.project.readers_community.entity.Book;
import com.project.readers_community.service.book_service.BooksServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BooksServiceImp booksServiceImp;

    @GetMapping("/api/books/search-and-save")
    public ResponseEntity<?> SearchAndSaveBook(@RequestParam String query, @RequestParam String userId) {
        try {
            Book book = booksServiceImp.fetchBookFromGoogle(query, userId);
            if (book != null) {
                return ResponseEntity.ok(book);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing request: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/DeleteBooksById/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
        try {
            booksServiceImp.deleteBookById(bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/GetAllbooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = booksServiceImp.getAllBooks();
        return ResponseEntity.ok(books);
    }




    @DeleteMapping("/api/DeleteAllBooks")
    public ResponseEntity<?> deleteAllBooks() {
        try {
            long deletedCount = booksServiceImp.deleteAllBooks();
            return ResponseEntity.ok(Map.of("message", "Successfully deleted " + deletedCount + " books"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No books to delete: " + e.getMessage());
        }
    }
}
