package com.project.readers_community.service.impl;

import com.mongodb.DuplicateKeyException;
import com.project.readers_community.handelException.exception.BadCredentialsException;
import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.ConflictException;
import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.mapper.UserMapper;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.enums.NotificationType;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.UserRequestLogin;
import com.project.readers_community.model.dto.request.UserRequestSignIn;
import com.project.readers_community.model.dto.response.UserResponse;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
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
    private BookService bookService;

    @Autowired
    private NotificationService notificationService;


@Override
public UserResponse signUp(UserRequestSignIn request) {
    try {
        User toDocument = UserMapper.mapToDocument(request);
        User user = this.userRepo.save(toDocument);
        return UserMapper.mapToResponse(user);
    } catch (DuplicateKeyException ex) {
        String message = ex.getMessage();

        if (message.contains("username")) {
            throw new ConflictException("Username '" + request.getUsername() + "' is already in use");
        } else if (message.contains("email")) {
            throw new ConflictException("Email '" + request.getEmail() + "' is already in use");
        } else {
            throw new ConflictException("Username or Email is already in use");
        }

    } catch (Exception e) {
        throw new ConflictException("Unexpected error occurred while signing up");
    }
}

    @Override
    public UserResponse login(UserRequestLogin request) {
        User user = userRepo.getByEmail(request.getEmail());
        if (user.getStatus() == Status.BLOCKED){
            throw new BadReqException("");
        }

        if (!user.getPassword().equals(request.getPassword().trim())) {
            throw new BadCredentialsException("Invalid password");
        }

        return UserMapper.mapToResponse(user);
    }

    @Override
    public UserResponse getById(String id) {
       User user = userRepo.getById(id);
        return UserMapper.mapToResponse(user);
    }

    @Override
    public UserResponse getByIdIfPresent(String id) {
        Optional<User> user = userRepo.getByIdIfPresent(id);
        return user.map(UserMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserResponse getByUsernameIfPresent(String username) {
        Optional<User> user = userRepo.getByUsernameIfPresent(username);
        return user.map(UserMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    @Override
    public UserResponse getByUsername(String username) {
        User user = userRepo.getByUsername(username);
        return UserMapper.mapToResponse(user);
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

//            if (notificationService != null) {
//                String message = follower.getUsername() + " started following you";
//                notificationService.createNotificationAsync(
//                        followingId,
//                        followerId,
//                        NotificationType.FOLLOW,
//                        message,
//                        null,
//                        null,
//                        null,
//                        null
//                );
//            }

            userRepo.save(follower);
            userRepo.save(following);
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
    public UserResponse update(String id, UserRequestSignIn request) {
        User user = userRepo.getById(id);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setProfilePicture(request.getProfilePicture());
        user.setBio(request.getBio());
        userRepo.save(user);
        return UserMapper.mapToResponse(userRepo.save(user));
    }

    @Override
    public UserResponse softDeleteById(String id) {
        User user = userRepo.getById(id);
        user.setStatus(Status.DELETED);
        userRepo.save(user);
        return UserMapper.mapToResponse(userRepo.save(user));
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        User user = userRepo.getById(id);
        userRepo.delete(user);
        return new MessageResponse("User deleted successfully");
    }


    @Override
    public MessageResponse addBookToFinishedList(String userId, String bookId) {
        User user = this.userRepo.getById(userId);
        this.bookService.getById(bookId);
        boolean isAdded = false;
        if (user.getFinishedBooks().contains(bookId)){
            user.getFinishedBooks().remove(bookId);
        } else {
            isAdded = true;
            user.getFinishedBooks().add(bookId);
        }
        return AssistantHelper.toMessageResponse(isAdded ? "Added Successfully." : "Removed Successfully.");
    }

    @Override
    public void addBookToWantToReadList(String userId, String bookId) {
        return;
    }

    @Override
    public void addBookToCurrentlyReadingList(String userId, String bookId) {
        return;
    }


    ///  switch to book service ///
    @Override
    public List<String> getWantToReadBooks(String userId) {
       User user = userRepo.getById(userId);
       user.getWantToReadBooks();
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
    /// end ///

}
