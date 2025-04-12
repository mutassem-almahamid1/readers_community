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
public class BooksServiceImp implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${google.books.api.key}")
    private String apiKey;

    @Override
    public List<Book> fetchBookFromGoogle(String query, String addedBy) {
        // Default parameters
        int maxResults = 40;
        int startIndex = 0;

        // Build URL with parameters
        String url = "https://www.googleapis.com/books/v1/volumes?q=";

        // Check if query is specific to subject
        if (query.startsWith("subject:")) {
            url += query;
        } else {
            url += "subject:" + query;
        }

        url += "&maxResults=" + maxResults + "&startIndex=" + startIndex + "&key=" + apiKey;

        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<Book> savedBooks = new ArrayList<>();

        try {
            GoogleBooksResponse response = objectMapper.readValue(jsonResponse, GoogleBooksResponse.class);
            if (response.getItems() != null && !response.getItems().isEmpty()) {
                for (GoogleBooksResponse.Item item : response.getItems()) {
                    GoogleBooksResponse.VolumeInfo volumeInfo = item.getVolumeInfo();

                    // Skip items with missing essential information
                    if (volumeInfo == null || volumeInfo.getTitle() == null) {
                        continue;
                    }

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

                    // Check if book already exists by ISBN
                    Book existingBook = null;
                    if (isbn != null) {
                        Optional<Book> existingBookByIsbn = bookRepository.findByIsbn(isbn);
                        if (existingBookByIsbn.isPresent()) {
                            savedBooks.add(existingBookByIsbn.get());
                            continue; // Skip to next book if this one exists
                        }
                    }

                    // Check if book already exists by title
                    String title = volumeInfo.getTitle();
                    Optional<Book> existingBookByTitle = bookRepository.findByTitle(title);

                    if (existingBookByTitle.isPresent()) {
                        existingBook = existingBookByTitle.get();
                        // If we found ISBN but book in DB doesn't have it, update the book
                        if (existingBook.getIsbn() == null && isbn != null) {
                            existingBook.setIsbn(isbn);
                            existingBook = bookRepository.save(existingBook);
                        }
                        savedBooks.add(existingBook);
                        continue; // Skip to next book
                    }

                    // Create new book if it doesn't exist
                    Book newBook = new Book();
                    newBook.setTitle(title);
                    newBook.setAuthor(volumeInfo.getAuthors() != null ? String.join(", ", volumeInfo.getAuthors()) : "غير معروف");
                    newBook.setCategory(volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty() ?
                                                                       volumeInfo.getCategories().get(0) : null);
                    newBook.setDescription(volumeInfo.getDescription());
                    newBook.setCategories(volumeInfo.getCategories() != null ? volumeInfo.getCategories() : new ArrayList<>());
                    newBook.setPublisher(volumeInfo.getPublisher());
                    newBook.setPublishedDate(volumeInfo.getPublishedDate());
                    newBook.setCoverImage(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null);
                    newBook.setAddedBy(addedBy);
                    newBook.setReviewCount(0);
                    newBook.setAvgRating(0.0);
                    newBook.setIsbn(isbn);

                    // Save and add to result list
                    Book savedBook = bookRepository.save(newBook);
                    savedBooks.add(savedBook);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedBooks;
    }

    @Override
    public void deleteBookById(String bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
        } else {
            throw new RuntimeException("Book with ID " + bookId + " not found");
        }
    }

    @Override
    public long deleteAllBooks() {
        if (bookRepository.existsBy()) {
            long count = bookRepository.count();
            bookRepository.deleteAll();
            return count;
        } else {
            throw new RuntimeException("No books found to delete");
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book with ISBN " + isbn + " not found"));
    }
}
