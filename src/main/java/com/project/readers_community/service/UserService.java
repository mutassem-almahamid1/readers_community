package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.UpdateUserRequest;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.model.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse signUp(UserRequestSignIn request);

//    UserResponse login(UserRequestLogin request);

    UserResponse getById(String id);

//    UserResponse getByIdIfPresent(String id);

//    UserResponse getByUsernameIfPresent(String username);

    List<UserResponse> getByAll();

    Page<UserResponse> getByAllPage(int page, int size);

    MessageResponse update(String id, UpdateUserRequest request);

    MessageResponse softDeleteById(String id);

    MessageResponse hardDeleteById(String id);

    List<BookResponse> getWantToReadBooks(String userId);

    List<BookResponse> getFinishedBooks(String userId);

    List<BookResponse> getCurrentlyReadingBooks(String userId);

    MessageResponse updateStatus(String id, Status status);

    UserResponse getByUsername(String username);

    List<UserResponse> getAllFollowingById(String id);

    List<UserResponse> getAllFollowersById(String id);

    MessageResponse addBookToFinishedList(String userId, String bookId);

    MessageResponse addBookToWantToReadList(String userId, String bookId);

    MessageResponse addBookToCurrentlyReadingList(String userId, String bookId);

    MessageResponse followUser(String followerId, String followingId);

    void unfollowUser(String followerId, String followingId);
}