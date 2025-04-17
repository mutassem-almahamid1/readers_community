package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    BookResponse createByUserName(BookRequest request, String addedById);
    BookResponse getById(String id);
    List<BookResponse> getByAll();
    Page<BookResponse> getByAllPage(int page, int size);
    BookResponse update(String id, BookRequest request);
    BookResponse softDeleteById(String id);
    MessageResponse hardDeleteById(String id);
    List<BookResponse> getByCategory(String category);
}