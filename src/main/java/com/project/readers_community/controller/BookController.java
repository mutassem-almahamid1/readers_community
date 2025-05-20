package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.request.SearchBookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.CategoryResponse;
import com.project.readers_community.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;



@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    @Autowired
    private BookService service;

//    @PostMapping("/category")
//    public ResponseEntity<List<Book>> searchBooksByCategory(@RequestParam String category) {
//        List<Book> book = service.searchBooksByCategory(category);
//        return new ResponseEntity<>(book, HttpStatus.CREATED);
//    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookResponse>> getBookByName(@RequestParam String name) {
        return ResponseEntity.ok(this.service.getByNameContainingIgnoreCase(name));
    }

    @GetMapping("/name/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BookResponse>> getBookByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.service.getByNameContainingIgnoreCase(name, page, size));
    }

    @PostMapping("/search/page")
    public ResponseEntity<Page<BookResponse>> searchPage(
            @Valid @RequestBody SearchBookRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.service.searchPage(request, page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
            @Valid @RequestBody SearchBookRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.service.search(request));
    }

//    @GetMapping("/title")
//    public ResponseEntity<BookResponse> getByTitle(@RequestParam String name) {
//        return ResponseEntity.ok(service.getByTitle(name));
//    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookResponse>> getAllBook() {
        return ResponseEntity.ok(service.getByAll());
    }

    @GetMapping("/category")
    public ResponseEntity<List<BookResponse>> getBookByCategory(@RequestParam String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @GetMapping("/top")
    public ResponseEntity<List<BookResponse>> getTopRatedBooks() {
        List<BookResponse> books = service.getTopRatedBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/personalized-recommendations")
    public ResponseEntity<List<BookResponse>> getPersonalizedRecommendations(@RequestParam String userId) {
        List<BookResponse> books = service.getPersonalizedRecommendations(userId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/trending-month")
    public ResponseEntity<List<BookResponse>> getTrendingBooksThisMonth() {
        List<BookResponse> books = service.getTrendingBooksThisMonth();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/friend-recommendations")
    public ResponseEntity<List<BookResponse>> getFriendRecommendations(@RequestParam String userId) {
        List<BookResponse> books = service.getFriendRecommendations(userId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BookResponse>> getAllBookByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateBook(@PathVariable String id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/soft/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> softDeleteBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.softDeleteById(id));
    }

//    @DeleteMapping("/hard/{id}")
//    public ResponseEntity<MessageResponse> hardDeleteBookById(@PathVariable String id) {
//        return ResponseEntity.ok(service.hardDeleteById(id));
//    }
}

