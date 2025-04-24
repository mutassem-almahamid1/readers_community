package com.project.readers_community.repository;

import com.project.readers_community.model.document.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

    User save(User user);

    List<User> saveAll(List<User> users);

    Optional<User> getByIdIfPresent(String id);

    User getById(String id);

    Optional<User> getByUsername(String username);

    Optional<User> getByUsernameIfPresent(String username);

    List<User> getAll();

    Page<User> getAllPage(PageRequest pageRequest);

    void deleteById(String id);

    void delete(User user);
}

