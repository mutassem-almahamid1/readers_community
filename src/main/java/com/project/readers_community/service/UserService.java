package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse signUp(UserRequestSignIn request);

    UserResponse login(UserRequestLogin request);

    UserResponse getById(String id);

    UserResponse getByIdIfPresent(String id);

    UserResponse getByUsernameIfPresent(String username);

    List<UserResponse> getByAll();

    Page<UserResponse> getByAllPage(int page, int size);

    UserResponse update(String id, UserRequestSignIn request);

    UserResponse softDeleteById(String id);

    MessageResponse hardDeleteById(String id);

    List<String> getWantToReadBooks(String userId);

    List<String> getFinishedBooks(String userId);

    List<String> getCurrentlyReadingBooks(String userId);

    UserResponse getByUsername(String username);

    List<UserResponse> getAllFollowingById(String id);

    List<UserResponse> getAllFollowersById(String id);


    void addBookToFinishedList(String userId, String bookId);

    void addBookToWantToReadList(String userId, String bookId);

    void addBookToCurrentlyReadingList(String userId, String bookId);



    void followUser(String followerId, String followingId);

    void unfollowUser(String followerId, String followingId);
}