package com.project.readers_community.service.impl;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.mapper.CategoryMapper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.model.dto.request.CategoryRequest;
import com.project.readers_community.model.dto.response.CategoryResponse;
import com.project.readers_community.repository.CategoryRepo;
import com.project.readers_community.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
//        return this.categoryMapper.mapToResponse(this.categoryRepo.save(this.categoryMapper.mapToDocument(request)));
        Category toDocument = this.categoryMapper.mapToDocument(request);
        Category category = this.categoryRepo.save(toDocument);
        CategoryResponse categoryResponse = this.categoryMapper.mapToResponse(category);
        return categoryResponse;
    }

    @Override
    public CategoryResponse getById(String id) {
        Category category = this.categoryRepo.getById(id);
        CategoryResponse categoryResponse = this.categoryMapper.mapToResponse(category);
        return categoryResponse;
    }

    @Override
    public CategoryResponse getByName(String name) {
        Category category = this.categoryRepo.getByName(name);
        CategoryResponse categoryResponse = this.categoryMapper.mapToResponse(category);
        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> getByAll() {
        List<Category> categories = this.categoryRepo.getAll();
        List<CategoryResponse> categoryResponses = categories
                .stream()
                .map(category -> this.categoryMapper.mapToResponse(category))
                .collect(Collectors.toList());
        return categoryResponses;
    }

    @Override
    public Page<CategoryResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> categories = this.categoryRepo.getAllPage(pageRequest);
        Page<CategoryResponse> categoryResponses = categories
                .map(category -> this.categoryMapper.mapToResponse(category));
        return categoryResponses;
    }

    @Override
    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = this.categoryRepo.getById(id);
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription().trim());
        category.setUpdatedAt(LocalDateTime.now());
        Category categorySaved = this.categoryRepo.save(category);
        CategoryResponse categoryResponse = this.categoryMapper.mapToResponse(categorySaved);
        return categoryResponse;
    }

    @Override
    public CategoryResponse softDeleteById(String id) {
        Category category = this.categoryRepo.getById(id);
        category.setStatus(Status.DELETED);
        category.setDeletedAt(LocalDateTime.now());
        Category categorySaved = this.categoryRepo.save(category);
        CategoryResponse categoryResponse = this.categoryMapper.mapToResponse(categorySaved);
        return categoryResponse;
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        Category category = this.categoryRepo.getById(id);
        this.categoryRepo.deleteById(category.getId());
        return MessageResponse.builder().message("Category deleted successfully.").build();
    }
}
