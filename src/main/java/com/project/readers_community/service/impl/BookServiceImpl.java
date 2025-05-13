package com.project.readers_community.service.impl;

import com.project.readers_community.mapper.BookMapper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
import com.project.readers_community.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CategoryService categoryService;

    private final String apiKey = "AIzaSyCCybPucK-_tphMJf6fowwNaLLFw-FY7sE";
    private final String baseUrl = "https://www.googleapis.com/books/v1/volumes";

    @Override
    public List<Book> searchBooksByCategory(String category) {
        String url = baseUrl + "?q=subject:" + category + "&key=" + apiKey + "&maxResults=40";
        BookApiResponse response = restTemplate.getForObject(url, BookApiResponse.class);

        if (response == null || response.getItems() == null) {
            return List.of();
        }

        List<Book> books = Arrays.stream(response.getItems())
                .map(item -> {
                    VolumeInfo volumeInfo = item.getVolumeInfo();
                    return Book.builder()
                            .id(item.getId())
                            .title(volumeInfo.getTitle())
                            .author(Arrays.toString(volumeInfo.getAuthors() != null ? volumeInfo.getAuthors() : new String[]{}))
                            .description(volumeInfo.getDescription())
                            .coverImage(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null)
                            .createdAt(LocalDateTime.now())
                            .status(Status.ACTIVE)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        books.forEach(this::save);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getCreatedAt() == null) book.setCreatedAt(LocalDateTime.now());
        return bookRepo.save(book);
    }


    @Override
    public BookResponse create(BookRequest request, String addedById) {
        Book book = BookMapper.mapToDocument(request, addedById);
        this.categoryService.getById(book.getCategory());
        Book savedBook = bookRepo.save(book);
        return BookMapper.mapToResponse(savedBook);
    }

    @Override
    public BookResponse getById(String id) {
        Book book = bookRepo.getById(id);
        return BookMapper.mapToResponse(book);
    }

    @Override
    public BookResponse getByTitle(String name) {
        Book book = bookRepo.getByTitle(name);
        return BookMapper.mapToResponse(book);
    }

    @Override
    public List<BookResponse> getByAll() {
        List<Book> books = bookRepo.getAll();
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books = bookRepo.getAllPage(pageRequest);
        return books.map(BookMapper::mapToResponse);
    }

    @Override
    public BookResponse update(String id, BookRequest request) {
        Book book = bookRepo.getById(id);
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        book.setCoverImage(request.getCoverImageUrl() != null ? request.getCoverImageUrl().trim() : null);
        if (request.getCategory() != null && !request.getCategory().equals(book.getCategory())) {
            book.setCategory(request.getCategory());
        }
        book.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return BookMapper.mapToResponse(updatedBook);
    }

    @Override
    public BookResponse softDeleteById(String id) {
        Book book = bookRepo.getById(id);
        book.setStatus(Status.DELETED);
        book.setDeletedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return BookMapper.mapToResponse(updatedBook);
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        Book book = bookRepo.getById(id);
        bookRepo.deleteById(id);
        return MessageResponse.builder().message("Book deleted successfully").build();
    }

    @Override
    public List<BookResponse> getByCategory(String category) {
        Optional<Book> books=bookRepo.getAllByCategory(category);
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBookSuggestions(int limit) {
        List<Book> books = bookRepo.findTopBooksByRatingAndReviews(limit);
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getTrendingBooks(int limit) {
        List<Book> books = bookRepo.findTrendingBooksForCurrentMonth(limit);
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<BookResponse> getPersonalizedBookSuggestions(String userId, int limit) {
        User user = userRepo.getById(userId);

        // Use finishedBooks directly as they are already the category values (strings)
        List<String> readCategories = user.getFinishedBooks();

        if (readCategories.isEmpty()) {
            return getBookSuggestions(limit);
        }

        List<Book> suggestedBooks = bookRepo.findTopBooksByCategories(readCategories, limit);
        return suggestedBooks.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBookReviewAndRating(String bookId, int reviewCount, double ratingTotal) {
        Book book = bookRepo.getById(bookId);
        book.setReviewCount(reviewCount);
        book.setAvgRating(reviewCount > 0 ? (ratingTotal / reviewCount) : 0);
        bookRepo.save(book);
    }













    /// /// // /////////////////////////////////////////////

    private static class BookApiResponse {
        private Item[] items;

        public Item[] getItems() {
            return items;
        }
    }
    private static class Item {
        private String id;
        private VolumeInfo volumeInfo;
        private Status status=Status.ACTIVE;
        private LocalDateTime createdAt;

        public String getId() {
            return id;
        }

        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }
    }

    private static class VolumeInfo {
        private String title;
        private String[] authors;
        private String description;
        private ImageLinks imageLinks;

        public String getTitle() {
            return title;
        }



        public String[] getAuthors() {
            return authors;
        }

        public String getDescription() {
            return description;
        }

        public ImageLinks getImageLinks() {
            return imageLinks;
        }
    }

    private static class ImageLinks {
        private String thumbnail;

        public String getThumbnail() {
            return thumbnail;
        }
    }
}
