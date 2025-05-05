package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.UserMapper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.NotificationType;
import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.NotificationService;
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

    @Autowired
    private NotificationService notificationService;


@Override
public UserResponse signUp(UserRequestSignIn request) {
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
    public UserResponse login(UserRequestLogin request) {
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
    public UserResponse getByIdIfPresent(String id) {
        Optional<User> user = userRepo.getByIdIfPresent(id);
        return user.map(userMapper::mapToResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserResponse getByUsernameIfPresent(String username) {
        Optional<User> user = userRepo.getByUsernameIfPresent(username);
        return user.map(userMapper::mapToResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public UserResponse getByUsername(String username) {
        Optional<User> user = userRepo.getByUsername(username);
        return user.map(userMapper::mapToResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public List<UserResponse> getAllFollowingById(String id) {
        List<User> users = userRepo.getAllFollowingById(id);
        List<UserResponse> userResponses = users
                .stream()
                .map(user->this.userMapper.mapToResponse(user))
                .collect(Collectors.toList());
        return userResponses;
    }


    @Override
    public List<UserResponse> getAllFollowersById(String id) {
        List<User> users = userRepo.getAllFollowersById(id);
        List<UserResponse> userResponses = users
                .stream()
                .map(user->this.userMapper.mapToResponse(user))
                .collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public void followUser(String followerId, String followingId) {
        User follower = userRepo.getById(followerId);
        User following = userRepo.getById(followingId);

        if (follower == null || following == null) {
            throw new NotFoundException("User not found");
        }

        if (!follower.getFollowing().contains(followingId)) {
            follower.getFollowing().add(followingId);
            following.getFollowers().add(followerId);

            // Create notification when a user follows another user
            if (notificationService != null) { // Ensure notificationService is not null
                String message = follower.getUsername() + " started following you";
                notificationService.createNotificationAsync(
                        followingId, // Recipient
                        followerId,  // Trigger user
                        NotificationType.FOLLOW,
                        message,
                        null, // No review
                        null, // No comment
                        null, // No book
                        null  // No post
                );
            }

            userRepo.save(follower);
            userRepo.save(following);
        }
    }


    @Override
    public void unfollowUser(String followerId, String followingId) {
        User follower = userRepo.getById(followerId);
        User following = userRepo.getById(followingId);

        if (follower == null || following == null) {
            throw new NotFoundException("User not found");
        }

        if (follower.getFollowing().contains(followingId)) {
            follower.getFollowing().remove(followingId);
            following.getFollowers().remove(followerId);
            userRepo.save(follower);
            userRepo.save(following);
        }
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
    public UserResponse update(String id, UserRequestSignIn request) {
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
