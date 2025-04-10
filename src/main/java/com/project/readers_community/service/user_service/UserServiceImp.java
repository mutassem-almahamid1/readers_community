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
        System.out.println("Attempting to add book " + bookId + " to user " + userId);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + userId);
            return false;
        }
        if (bookOptional.isEmpty()) {
            System.out.println("Book not found: " + bookId);
            return false;
        }

        User user = userOptional.get();
        Book book = bookOptional.get();

        if (user.getWantToReadBooks() == null) {
            user.setWantToReadBooks(new ArrayList<>());
            System.out.println("Initialized wantToReadBooks for user " + userId);
        }

        boolean bookExists = user.getWantToReadBooks().stream()
                .anyMatch(b -> b.getId().equals(bookId));
        System.out.println("Book exists in list: " + bookExists);

        if (!bookExists) {
            user.getWantToReadBooks().add(book);
            userRepository.save(user);
            System.out.println("Book added and user saved");
            return true;
        }
        System.out.println("Book already exists in the list");
        return false;
    }

    @Override
    @Transactional
    public boolean removeBookFromWantToRead(String userId, String bookId) {
        System.out.println("Attempting to remove book " + bookId + " from user " + userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + userId);
            return false;
        }

        User user = userOptional.get();
        if (user.getWantToReadBooks() == null || user.getWantToReadBooks().isEmpty()) {
            System.out.println("No books in wantToReadBooks for user: " + userId);
            return false;
        }

        System.out.println("Current books in wantToReadBooks: " + user.getWantToReadBooks());
        boolean removed = user.getWantToReadBooks().removeIf(book -> {
            boolean match = book.getId().equals(bookId);
            System.out.println("Comparing book ID " + book.getId() + " with " + bookId + ": " + match);
            return match;
        });

        if (removed) {
            userRepository.save(user);
            System.out.println("Book removed and user saved");
            return true;
        } else {
            System.out.println("Book not found in the list or removal failed");
            return false;
        }
    }

    @Override
    public List<Book> getWantToReadBooks(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        return user.getWantToReadBooks() != null ? user.getWantToReadBooks() : new ArrayList<>();
    }

    @Override
    @Transactional
    public boolean addBookToCurrentlyReading(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (userOptional.isEmpty() || bookOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        Book book = bookOptional.get();

        // تهيئة القائمة لو null
        if (user.getCurrentlyReadingBooks() == null) {
            user.setCurrentlyReadingBooks(new ArrayList<>());
        }

        // التحقق من التكرار
        boolean bookExists = user.getCurrentlyReadingBooks().stream()
                .anyMatch(b -> b.getId().equals(bookId));

        if (!bookExists) {
            user.getCurrentlyReadingBooks().add(book);
            userRepository.save(user);
            return true;
        }
        return false; // الكتاب موجود بالفعل
    }

    @Override
    @Transactional
    public boolean removeBookFromCurrentlyReading(String userId, String bookId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        if (user.getCurrentlyReadingBooks() == null || user.getCurrentlyReadingBooks().isEmpty()) {
            return false;
        }

        boolean removed = user.getCurrentlyReadingBooks().removeIf(book -> book.getId().equals(bookId));
        if (removed) {
            userRepository.save(user);
            return true;
        }
        return false; // الكتاب غير موجود في القائمة
    }

    @Override
    public List<Book> getCurrentlyReadingBooks(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        return user.getCurrentlyReadingBooks() != null ? user.getCurrentlyReadingBooks() : new ArrayList<>();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.get();
    }

}