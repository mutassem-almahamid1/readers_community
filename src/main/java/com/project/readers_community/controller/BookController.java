package com.project.readers_community.controller;

import com.project.readers_community.entity.Book;
import com.project.readers_community.service.GoogleBooksService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class BookController {

    @Autowired
    private GoogleBooksService googleBooksService;

    @GetMapping("/api/books/search")
    public Book searchBook(@RequestParam String query, @RequestParam String userId) {
        return googleBooksService.fetchBookFromGoogle(query, userId);
    }
}