package com.project.readers_community.controller;

import com.project.readers_community.entity.Book;
import com.project.readers_community.service.book_service.BooksServiceImp;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BooksServiceImp booksServiceImp;

    @GetMapping("/search")
    public ResponseEntity<?> searchAndSaveBook(@RequestParam String query, @RequestParam String userId) {
        try {
            List<Book> books = booksServiceImp.fetchBookFromGoogle(query, userId);
            if (books != null && !books.isEmpty()) {
                return ResponseEntity.ok(books);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No books found for query: " + query));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error processing request: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
        try {
            booksServiceImp.deleteBookById(bookId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Book deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }

    @GetMapping("/GetAllbooks")
    public ResponseEntity<Map<String, Object>> getAllBooks() {
        List<Book> books = booksServiceImp.getAllBooks();
        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("count", books.size());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/DeleteAllBooks")
    public ResponseEntity<?> deleteAllBooks() {
        try {
            long deletedCount = booksServiceImp.deleteAllBooks();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Successfully deleted " + deletedCount + " books"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "No books to delete: " + e.getMessage()
                ));
        }
    }
    
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        Book book = booksServiceImp.findBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "No book found with ISBN: " + isbn
                ));
        }
    }

    @PutMapping("/savebook")
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        try {
            Book savedBook = booksServiceImp.saveBook(book);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Book saved successfully",
                "book", savedBook
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getBookByTitle(@PathVariable String title) {
        Book book = booksServiceImp.findBookByTitle(title);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "No book found with title: " + title
                ));
        }
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        Book book = booksServiceImp.findBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "No book found with ID: " + id
                ));
        }
    }


}
