package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.dto.request.CategoryRequest;
import com.project.readers_community.model.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);

    CategoryResponse getById(String id);

    CategoryResponse getByName(String name);

    Category getByNameForImport(String name);

    List<CategoryResponse> getByAll();

    Page<CategoryResponse> getByAllPage(int page, int size);

    CategoryResponse update(String id, CategoryRequest request);

    CategoryResponse softDeleteById(String id);

    MessageResponse hardDeleteById(String id);
}
