package com.project.readers_community.repository.impl;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Category;
import com.project.readers_community.repository.CategoryRepo;
import com.project.readers_community.repository.mongo.CategoryRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepoImpl implements CategoryRepo {
    @Autowired
    private CategoryRepoMongo repoMongo;

    @Override
    public Category save(Category category) {
        return repoMongo.save(category);
    }

    @Override
    public List<Category> saveAll(List<Category> categories) {
        return repoMongo.saveAll(categories);
    }

    @Override
    public Optional<Category> getByIdIfPresent(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public Optional<Category> getByNameIfPresent(String name) {
        return repoMongo.findByNameAndStatus(name, Status.ACTIVE);
    }


    @Override
    public Category getById(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public Category getByName(String name) {
        return repoMongo.findByNameAndStatus(name, Status.ACTIVE).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public List<Category> getAll() {
        return repoMongo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Page<Category> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatus(Status.ACTIVE, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(Category category) {
        repoMongo.delete(category);
    }
}
