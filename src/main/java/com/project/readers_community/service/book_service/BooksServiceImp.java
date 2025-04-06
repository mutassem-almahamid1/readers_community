package com.project.readers_community.service.book_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.readers_community.entity.Book;
import com.project.readers_community.dto.book_dto.GoogleBooksResponse;
import com.project.readers_community.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BooksServiceImp {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${google.books.api.key}")
    private String apiKey;


    public Book fetchBookFromGoogle(String query, String addedBy) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            GoogleBooksResponse response = objectMapper.readValue(jsonResponse, GoogleBooksResponse.class);
            if (response.getItems() != null) {
                GoogleBooksResponse.VolumeInfo volumeInfo = response.getItems().get(0).getVolumeInfo();

                // Extracting ISBN from industry identifiers
                String isbn = null;
                if (volumeInfo.getIndustryIdentifiers() != null) {
                    for (GoogleBooksResponse.IndustryIdentifier identifier : volumeInfo.getIndustryIdentifiers()) {
                        if ("ISBN_13".equals(identifier.getType())) {
                            isbn = identifier.getIdentifier();
                            break;
                        } else if ("ISBN_10".equals(identifier.getType()) && isbn == null) {
                            isbn = identifier.getIdentifier();
                        }
                    }
                }
                
                // Check if book already exists by ISBN if available
                if (isbn != null) {
                    Optional<Book> existingBookByIsbn = bookRepository.findByIsbn(isbn);
                    if (existingBookByIsbn.isPresent()) {
                        return existingBookByIsbn.get();
                    }
                }
                
                // Check if book already exists by title
                String title = volumeInfo.getTitle();
                Optional<Book> existingBook = bookRepository.findByTitle(title);
                
                if (existingBook.isPresent()) {
                    // Update ISBN if it was missing
                    Book book = existingBook.get();
                    if (book.getIsbn() == null && isbn != null) {
                        book.setIsbn(isbn);
                        return bookRepository.save(book);
                    }
                    return book;
                }

                // Create new Book if it doesn't exist
                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(volumeInfo.getAuthors() != null ? String.join(", ", volumeInfo.getAuthors()) : "غير معروف");
                book.setCategory(volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty() ?
                                                                       volumeInfo.getCategories().get(0) : null);
                book.setDescription(volumeInfo.getDescription());
                book.setCategories(volumeInfo.getCategories() != null ? volumeInfo.getCategories() : new ArrayList<>());
                book.setPublisher(volumeInfo.getPublisher());
                book.setPublishedDate(volumeInfo.getPublishedDate());
                book.setCoverImage(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null);
                book.setAddedBy(addedBy);
                book.setReviewCount(0);
                book.setAvgRating(0.0);
                book.setIsbn(isbn);

                return bookRepository.save(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteBookById(String bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookRepository.deleteById(bookId);
        } else {
            throw new RuntimeException("الكتاب غير موجود بالمعرف: " + bookId);
        }
    }


    public long deleteAllBooks() {
        if (!bookRepository.existsBy()) {
            throw new RuntimeException("لا توجد كتب للحذف");
        }

        long count = bookRepository.count();
        bookRepository.deleteAll();
        return count;
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book findBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.orElse(null);
    }

    public Book findBookById(String bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.orElse(null);
    }
}
