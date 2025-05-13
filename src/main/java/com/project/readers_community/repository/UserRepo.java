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

    Optional<User> getByEmail(String email);

    Optional<User> getByUsernameIfPresent(String username);

    Optional<User> getByEmailIfPresent(String email);

    List<User> getAll();

    Page<User> getAllPage(PageRequest pageRequest);

    List<User> getAllFollowingById(String id);

    List<User> getAllFollowersById(String id);

    void addBookToFinishedList(String userId, String bookId);

    void addBookToWantToReadList(String userId, String bookId);

    void addBookToCurrentlyReadingList(String userId, String bookId);

    void deleteById(String id);

    void delete(User user);
}
