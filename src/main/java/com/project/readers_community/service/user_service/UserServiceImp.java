package com.project.readers_community.service.user_service;

import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;
import com.project.readers_community.repository.BookRepository;
import com.project.readers_community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // تسجيل مستخدم جديد
    @Override
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword()); // ملاحظة: يفضل تشفير الباسوورد هنا
        user.setRole(registrationDTO.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setWantToReadBooks(new ArrayList<>()); // تهيئة القائمة عند الإنشاء
        user.setCurrentlyReadingBooks(new ArrayList<>()); // تهيئة القائمة عند الإنشاء
        user.setFinishedBooks(new ArrayList<>()); // تهيئة قائمة الكتب المنتهية
        return userRepository.save(user);
    }

    // تسجيل الدخول
    @Override
    public UserLoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user == null || !loginDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return new UserLoginResponseDTO(user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    @Transactional
    public boolean addBookToWantToRead(String userId, String bookId) {
        System.out.println("Attempting to add book " + bookId + " to user " + userId + " 'Want to Read' list");
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        
        if (userOptional.isPresent() && bookOptional.isPresent()) {
            User user = userOptional.get();
            Book book = bookOptional.get();
            
            // Check if book is already in the list
            if (user.getWantToReadBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in the list
            }
            
            user.getWantToReadBooks().add(book);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean removeBookFromWantToRead(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean removed = user.getWantToReadBooks().removeIf(book -> book.getId().equals(bookId));
            if (removed) {
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Book> getWantToReadBooks(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getWantToReadBooks();
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }
    
    @Override
    @Transactional
    public boolean addBookToCurrentlyReading(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        
        if (userOptional.isPresent() && bookOptional.isPresent()) {
            User user = userOptional.get();
            Book book = bookOptional.get();
            
            // Check if book is already in the list
            if (user.getCurrentlyReadingBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in the list
            }
            
            // If the book is in "Want to Read" list, remove it
            user.getWantToReadBooks().removeIf(b -> b.getId().equals(bookId));
            
            user.getCurrentlyReadingBooks().add(book);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean removeBookFromCurrentlyReading(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean removed = user.getCurrentlyReadingBooks().removeIf(book -> book.getId().equals(bookId));
            if (removed) {
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Book> getCurrentlyReadingBooks(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getCurrentlyReadingBooks();
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }
    
    @Override
    @Transactional
    public boolean addBookToFinishedBooks(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        
        if (userOptional.isPresent() && bookOptional.isPresent()) {
            User user = userOptional.get();
            Book book = bookOptional.get();
            
            // Check if book is already in the list
            if (user.getFinishedBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in the list
            }
            
            // If the book is in "Currently Reading" list, remove it
            user.getCurrentlyReadingBooks().removeIf(b -> b.getId().equals(bookId));
            // If the book is in "Want to Read" list, remove it
            user.getWantToReadBooks().removeIf(b -> b.getId().equals(bookId));
            
            user.getFinishedBooks().add(book);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean removeBookFromFinishedBooks(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean removed = user.getFinishedBooks().removeIf(book -> book.getId().equals(bookId));
            if (removed) {
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Book> getFinishedBooks(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getFinishedBooks();
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }
}
