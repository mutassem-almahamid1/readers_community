package com.project.readers_community.repository;

import com.project.readers_community.model.document.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo {
    Category save(Category category);

    List<Category> saveAll(List<Category> categories);

    Optional<Category> getByIdIfPresent(String id);

    Category getByNameIfPresent(String name);

    Category getByIdIgnoreStatus(String id);

    Category getByName(String name);

    Category getById(String id);

    List<Category> getAll();

    Page<Category> getAllPage(PageRequest pageRequest);

    void deleteById(String id);

    void delete(Category category);
}
