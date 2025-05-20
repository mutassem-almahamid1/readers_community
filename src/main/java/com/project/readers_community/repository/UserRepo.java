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

    User getByUsername(String username);

    List<User> getByNameContainingIgnoreCase(String name);

    Page<User> getByNameContainingIgnoreCase(String name, PageRequest pageRequest);

    Optional<User> getByUsernameIgnoreStatus(String username);

    Optional<User> getByEmailIgnoreStatus(String email);

    User getByEmail(String email);

    User getByIdAndStatusNotDeleted(String id);

    boolean getByUsernameIfPresent(String username);

    User getByRefreashToken(String refreshToken);

    boolean getByEmailIfPresent(String email);

    List<User> getAll();

    Page<User> getAllPage(PageRequest pageRequest);

    List<User> getAllByIdIn(List<String> ids);

//    List<User> getAllFollowingById(String id);
//
//    List<User> getAllFollowersById(String id);
//
//    void addBookToFinishedList(String userId, String bookId);
//
//    void addBookToWantToReadList(String userId, String bookId);
//
//    void addBookToCurrentlyReadingList(String userId, String bookId);

    List<User> getAllByIdNotIn(List<String> ids);

    void deleteById(String id);

    void delete(User user);
}
