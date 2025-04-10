package com.project.readers_community.service.user_service;

import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;
import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);
    User registerUser(UserRegistrationDTO registrationDTO);
    UserLoginResponseDTO loginUser(UserLoginDTO loginDTO);
    
    // Methods for "Want to Read" list
    boolean addBookToWantToRead(String userId, String bookId);
    boolean removeBookFromWantToRead(String userId, String bookId);
    List<Book> getWantToReadBooks(String userId);
    
    // Methods for "Currently Reading" list
    boolean addBookToCurrentlyReading(String userId, String bookId);
    boolean removeBookFromCurrentlyReading(String userId, String bookId);
    List<Book> getCurrentlyReadingBooks(String userId);

    List<User> getAllUsers();

    User getUserById(String userId);
}
