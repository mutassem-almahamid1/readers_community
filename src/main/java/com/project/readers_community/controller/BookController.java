package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request, @RequestParam String addedByUserName) {
        return ResponseEntity.ok(service.createByUserName(request, addedByUserName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<BookResponse> getBookByName(@RequestParam String name) {
        return ResponseEntity.ok(service.getByName(name));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBook() {
        return ResponseEntity.ok(service.getByAll());
    }

    @GetMapping("/category")
    public ResponseEntity<List<BookResponse>> getBookByCategory(@RequestParam String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }



    @GetMapping("/page")
    public ResponseEntity<Page<BookResponse>> getAllBookByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable String id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<BookResponse> softDeleteBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.softDeleteById(id));
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<MessageResponse> hardDeleteBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.hardDeleteById(id));
    }
}