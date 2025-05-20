package com.project.readers_community.service.impl;

import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.ConflictException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.UserMapper;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.dto.request.NotificationRequest;
import com.project.readers_community.model.dto.request.UpdateUserRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.model.dto.response.NotificationResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
import com.project.readers_community.service.NotificationService;
import com.project.readers_community.service.UserService;
import com.project.readers_community.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookService bookService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;


    @Override
    public UserResponse signUp(UserRequestSignIn request) {
        User toDocument = UserMapper.mapToDocument(request);
        // Check if username is already taken
        if (userRepo.getByUsernameIfPresent(toDocument.getUsername())) {
            throw new ConflictException("Username is already taken");
        }

        // Check if email is already in use
        if (userRepo.getByEmailIfPresent(toDocument.getEmail())) {
            throw new ConflictException("Email is already in use");
        }

        toDocument.setPassword(passwordEncoder.encode(toDocument.getPassword()));
        User user = this.userRepo.save(toDocument);
        return UserMapper.mapToResponse(user);
    }

//    @Override
//    public UserResponse login(UserRequestLogin request) {
//        User user = userRepo.getByEmail(request.getEmail());
//        if (user.getStatus() == Status.BLOCKED){
//            throw new BadReqException("");
//        }
//
//        if (!user.getPassword().equals(request.getPassword().trim())) {
//            throw new BadCredentialsException("Invalid password");
//        }
//
//        return UserMapper.mapToResponse(user);
//    }

    @Override
    public UserResponse getById(String id) {
        User user = userRepo.getById(id);
        return UserMapper.mapToResponse(user);
    }

    @Override
    public MessageResponse updateStatus(String id, Status status) {
        User user = userRepo.getByIdAndStatusNotDeleted(id);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        this.userRepo.save(user);
        return AssistantHelper.toMessageResponse("Updated Successfully.");
    }

//    @Override
//    public UserResponse getByIdIfPresent(String id) {
//        Optional<User> user = userRepo.getByIdIfPresent(id);
//        return user.map(UserMapper::mapToResponse)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//    }

//    @Override
//    public UserResponse getByUsernameIfPresent(String username) {
//        Optional<User> user = userRepo.getByUsernameIfPresent(username);
//        return user.map(UserMapper::mapToResponse)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//    }


    @Override
    public UserResponse getByUsername(String username) {
        User user = userRepo.getByUsername(username);
        return UserMapper.mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllByName(String name) {
        List<User> users = this.userRepo.getByNameContainingIgnoreCase(name);
        return users
                .stream()
                .map(UserMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> getByNamePage(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepo.getByNameContainingIgnoreCase(name, pageRequest);
        return userPage
                .map(UserMapper::mapToResponse);
    }

    @Override
    public List<UserResponse> getAllByIdIn(List<String> ids) {
        List<User> users = this.userRepo.getAllByIdIn(ids);
        return users
                .stream()
                .map(UserMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllByIdNotIn(List<String> ids) {
        List<User> users = this.userRepo.getAllByIdNotIn(ids);
        return users.stream().map(User::getId).toList();
    }

    @Override
    public List<UserResponse> getAllFollowingById(String id) {
        User user = this.userRepo.getById(id);
        List<User> users = userRepo.getAllByIdIn(user.getFollowing());
        return users
                .stream()
                .map(UserMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<UserResponse> getAllFollowersById(String id) {
        User user = this.userRepo.getById(id);
        List<User> users = userRepo.getAllByIdIn(user.getFollowers());
        return users
                .stream()
                .map(UserMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse followUser(String followerId, String followingId) {
        User follower = userRepo.getById(followerId);
        User following = userRepo.getById(followingId);

        // Don't allow following yourself
        if (followerId.equals(followingId)) {
            throw new BadReqException("Cannot follow yourself");
        }

        if (!follower.getFollowing().contains(followingId)) {
            follower.getFollowing().add(followingId);
            following.getFollowers().add(followerId);

            userRepo.save(follower);
            userRepo.save(following);

            NotificationResponse notificationResponse = notificationService.create(NotificationRequest.builder()
                    .recipientId(followingId)
                    .triggerUserId(followerId)
                    .message(follower.getUsername() + " started following you")
                    .type(NotificationType.FOLLOW)
                    .build());

            this.webSocketNotificationService.notifyNewNotification(notificationResponse, followingId);

            return AssistantHelper.toMessageResponse("Following Successfully.");
        } else {
            follower.getFollowing().remove(followingId);
            following.getFollowers().remove(followerId);

            userRepo.save(follower);
            userRepo.save(following);
            return AssistantHelper.toMessageResponse("Unfollowing Successfully.");
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
        return users
                .stream()
                .map(UserMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepo.getAllPage(pageRequest);
        return userPage
                .map(UserMapper::mapToResponse);
    }

    @Override
    public MessageResponse update(String id, UpdateUserRequest request) {
        User user = userRepo.getById(id);
        user.setUsername(request.getUsername().trim());
        user.setFullName(request.getFullName().trim());
        user.setProfilePicture(request.getProfilePicture());
        user.setCoverPicture(request.getCoverPicture());
        user.setBio(request.getBio());
        userRepo.save(user);
        return new MessageResponse("User updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        User user = userRepo.getById(id);
        user.setStatus(Status.DELETED);
        userRepo.save(user);
        return new MessageResponse("User deleted successfully.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        User user = userRepo.getById(id);
        userRepo.delete(user);
        return new MessageResponse("User deleted successfully.");
    }


    @Override
    public MessageResponse addBookToFinishedList(String userId, String bookId) {
        User user = this.userRepo.getById(userId);
        this.bookService.getById(bookId);
        boolean isAdded = false;
        if (user.getFinishedBooks().contains(bookId)) {
            user.getFinishedBooks().remove(bookId);
        } else {
            isAdded = true;
            user.getFinishedBooks().add(bookId);
        }
        return AssistantHelper.toMessageResponse(isAdded ? "Added Successfully." : "Removed Successfully.");
    }

    @Override
    public MessageResponse addBookToWantToReadList(String userId, String bookId) {
        User user = this.userRepo.getById(userId);
        this.bookService.getById(bookId);
        boolean isAdded = false;
        if (user.getWantToReadBooks().contains(bookId)) {
            user.getWantToReadBooks().remove(bookId);
        } else {
            isAdded = true;
            user.getWantToReadBooks().add(bookId);
        }
        return AssistantHelper.toMessageResponse(isAdded ? "Added Successfully." : "Removed Successfully.");
    }

    @Override
    public MessageResponse addBookToCurrentlyReadingList(String userId, String bookId) {
        User user = this.userRepo.getById(userId);
        this.bookService.getById(bookId);
        boolean isAdded = false;
        if (user.getCurrentlyReadingBooks().contains(bookId)) {
            user.getCurrentlyReadingBooks().remove(bookId);
        } else {
            isAdded = true;
            user.getCurrentlyReadingBooks().add(bookId);
        }
        return AssistantHelper.toMessageResponse(isAdded ? "Added Successfully." : "Removed Successfully.");

    }


    ///  switch to book service ///
    @Override
    public List<BookResponse> getWantToReadBooks(String userId) {
        User user = userRepo.getById(userId);
        return this.bookService.getByAllByIdIn(user.getWantToReadBooks());
    }

    @Override
    public List<BookResponse> getFinishedBooks(String userId) {
        User user = userRepo.getById(userId);
        return this.bookService.getByAllByIdIn(user.getFinishedBooks());
    }

    @Override
    public List<BookResponse> getCurrentlyReadingBooks(String userId) {
        User user = userRepo.getById(userId);
        return this.bookService.getByAllByIdIn(user.getWantToReadBooks());
    }
    /// end ///

    @Override
    public void deleteBookFromAllUsers(String bookId) {
        List<User> users = userRepo.getAll();
        users.forEach(user -> {
            user.getFinishedBooks().remove(bookId);
            user.getCurrentlyReadingBooks().remove(bookId);
            user.getWantToReadBooks().remove(bookId);
        });
        this.userRepo.saveAll(users);
    }

}
