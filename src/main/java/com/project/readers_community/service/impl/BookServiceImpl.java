package com.project.readers_community.service.impl;

import com.project.readers_community.mapper.BookMapper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.CategoryRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookMapper bookMapper;


    @Override
    public BookResponse createByUserName(BookRequest request, String addedByUserName) {
        Category category = categoryRepo.getById(request.getCategoryId());
        Optional<User> addedBy = userRepo.getByUsername(addedByUserName);
        Book book = bookMapper.mapToDocument(request, category, addedBy.orElse(null));
        Book savedBook = bookRepo.save(book);
        return bookMapper.mapToResponse(savedBook);
    }

    @Override
    public BookResponse getById(String id) {
        Book book = bookRepo.getById(id);
        return bookMapper.mapToResponse(book);
    }

    @Override
    public BookResponse getByName(String name) {
        Book book = bookRepo.getByName(name);
        return bookMapper.mapToResponse(book);
    }

    @Override
    public List<BookResponse> getByAll() {
        List<Book> books = bookRepo.getAll();
        return books.stream()
                .map(bookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books = bookRepo.getAllPage(pageRequest);
        return books.map(bookMapper::mapToResponse);
    }

    @Override
    public BookResponse update(String id, BookRequest request) {
        Book book = bookRepo.getById(id);
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        book.setCoverImage(request.getCoverImageUrl() != null ? request.getCoverImageUrl().trim() : null);
        if (request.getCategoryId() != null && !request.getCategoryId().equals(book.getCategory().getId())) {
            Category category = categoryRepo.getById(request.getCategoryId());
            book.setCategory(category);
        }
        book.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return bookMapper.mapToResponse(updatedBook);
    }

    @Override
    public BookResponse softDeleteById(String id) {
        Book book = bookRepo.getById(id);
        book.setStatus(Status.DELETED);
        book.setDeletedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return bookMapper.mapToResponse(updatedBook);
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
                .map(bookMapper::mapToResponse)
                .collect(Collectors.toList());
    }


}