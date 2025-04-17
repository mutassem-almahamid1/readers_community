package com.project.readers_community.service.impl;

import com.project.readers_community.mapper.UserMapper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequest;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;


@Override
public UserResponse signUp(UserRequest request) {
    // التحقق من وجود مستخدم بنفس اسم المستخدم باستخدام UserRepo
    if (userRepo.getByUsername(request.getUsername().trim()).isPresent()) {
        throw new RuntimeException("Username is already in use");
    }

    try {
        User toDocument = this.userMapper.mapToDocument(request);
        User user = this.userRepo.save(toDocument);
        return this.userMapper.mapToResponse(user);
    } catch (Exception e) {
        throw new RuntimeException("Username '" + request.getUsername() + "' is already taken");
    }
}

    @Override
    public UserResponse login(UserRequest request) {
        Optional<User> userOptional = userRepo.getByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                return userMapper.mapToResponse(user);
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public UserResponse getById(String id) {
       User user = userRepo.getById(id);
        return userMapper.mapToResponse(user);
    }


    @Override
    public UserResponse getByUsername(String username) {
        Optional<User> user = userRepo.getByUsername(username);
        return user.map(userMapper::mapToResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResponse> getByAll() {
        List<User> users = userRepo.getAll();
        List<UserResponse> userResponses = users
                .stream()
                .map(user->this.userMapper.mapToResponse(user))
                .collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public Page<UserResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepo.getAllPage(pageRequest);
        Page<UserResponse> userResponsePage = userPage
                .map(user -> this.userMapper.mapToResponse(user));
        return userResponsePage;
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        User user = userRepo.getById(id);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setProfilePicture(request.getProfilePicture());
        user.setBio(request.getBio());
        user.setRole(request.getRole());
        userRepo.save(user);
        return userMapper.mapToResponse(userRepo.save(user));
    }

    @Override
    public UserResponse softDeleteById(String id) {
        User user = userRepo.getById(id);
        user.setStatus(Status.DELETED);
        userRepo.save(user);
        return userMapper.mapToResponse(userRepo.save(user));
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        User user = userRepo.getById(id);
        userRepo.delete(user);
        return new MessageResponse("User deleted successfully");
    }

    @Override
    public List<String> getWantToReadBooks(String userId) {
       User user = userRepo.getById(userId);
        return user.getWantToReadBooks() != null ? user.getWantToReadBooks() : List.of();
    }

    @Override
    public List<String> getFinishedBooks(String userId) {
        User user = userRepo.getById(userId);
        return user.getFinishedBooks() != null ? user.getFinishedBooks() : List.of();
    }

    @Override
    public List<String> getCurrentlyReadingBooks(String userId) {
        User user = userRepo.getById(userId);
        return user.getCurrentlyReadingBooks() != null ? user.getCurrentlyReadingBooks() : List.of();
    }


}
