package com.project.readers_community.service;

import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse signUp(UserRequest request);

    UserResponse login(UserRequest request);

    UserResponse getById(String id);

    List<UserResponse> getByAll();

    Page<UserResponse> getByAllPage(int page, int size);

    UserResponse update(String id, UserRequest request);

    UserResponse softDeleteById(String id);

    MessageResponse hardDeleteById(String id);

    List<String> getWantToReadBooks(String userId);

    List<String> getFinishedBooks(String userId);

    List<String> getCurrentlyReadingBooks(String userId);

    UserResponse getByUsername(String username);
}