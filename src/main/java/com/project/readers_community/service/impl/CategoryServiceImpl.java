package com.project.readers_community.service.impl;

import com.project.readers_community.model.enums.Status;
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

    @Override
    public CategoryResponse create(CategoryRequest request) {
//        return this.categoryMapper.mapToResponse(this.categoryRepo.save(this.categoryMapper.mapToDocument(request)));
        Category toDocument = CategoryMapper.mapToDocument(request);
        Category category = this.categoryRepo.save(toDocument);
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public CategoryResponse getById(String id) {
        Category category = this.categoryRepo.getById(id);
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public CategoryResponse getByName(String name) {
        Category category = this.categoryRepo.getByName(name);
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public Category getByNameForImport(String name) {
        return this.categoryRepo.getByName(name);
    }

    @Override
    public List<CategoryResponse> getByAll() {
        List<Category> categories = this.categoryRepo.getAll();
        return categories
                .stream()
                .map(CategoryMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> categories = this.categoryRepo.getAllPage(pageRequest);
        return categories
                .map(CategoryMapper::mapToResponse);
    }

    @Override
    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = this.categoryRepo.getById(id);
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription().trim());
        category.setImgUrl(request.getImgUrl());
        category.setUpdatedAt(LocalDateTime.now());
        Category categorySaved = this.categoryRepo.save(category);
        return CategoryMapper.mapToResponse(categorySaved);
    }

    @Override
    public CategoryResponse softDeleteById(String id) {
        Category category = this.categoryRepo.getById(id);
        category.setStatus(Status.DELETED);
        category.setDeletedAt(LocalDateTime.now());
        Category categorySaved = this.categoryRepo.save(category);
        return CategoryMapper.mapToResponse(categorySaved);
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        Category category = this.categoryRepo.getById(id);
        this.categoryRepo.deleteById(category.getId());
        return MessageResponse.builder().message("Category deleted successfully.").build();
    }
}
