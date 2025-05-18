package com.project.readers_community.controller;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.CategoryRequest;
import com.project.readers_community.model.dto.response.CategoryResponse;
import com.project.readers_community.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(this.service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.getById(id));
    }

    @GetMapping("{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(this.service.getByName(name));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(this.service.getByAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CategoryResponse>> getAllCategoryPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.getByAllPage(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<MessageResponse> softDeleteCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(this.service.softDeleteById(id));
    }

//    @DeleteMapping("/hard/{id}")
//    public ResponseEntity<MessageResponse> hardDeleteCategoryById(@PathVariable String id) {
//        return ResponseEntity.ok(this.service.hardDeleteById(id));
//    }
}
