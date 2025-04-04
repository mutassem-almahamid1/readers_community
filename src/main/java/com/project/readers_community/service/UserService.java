package com.project.readers_community.service;

import com.project.readers_community.dto.UserRegistrationDTO;
import com.project.readers_community.dto.UserLoginDTO;
import com.project.readers_community.dto.LoginResponseDTO;
import com.project.readers_community.dto.AddBookToListDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;
import com.project.readers_community.repository.BookRepository;
import com.project.readers_community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // تسجيل مستخدم جديد (موجود مسبقًا)
    public User registerUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // تسجيل الدخول (موجود مسبقًا)
    public LoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = generateJwtToken(user);
        return new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), token);
    }

    // دالة لإضافة كتاب إلى قائمة بناءً على العنوان
    public User addBookToList(String userId, AddBookToListDTO addBookDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String bookTitle = addBookDTO.getBookTitle();
        String listType = addBookDTO.getListType().toLowerCase();

        // التحقق من وجود الكتاب في قاعدة البيانات
        Book book = bookRepository.findByTitle(bookTitle);
        if (book == null) {
            throw new RuntimeException("Book with title '" + bookTitle + "' not found");
        }

        // إزالة الكتاب من القوائم الأخرى (اختياري لضمان عدم التكرار)
        user.getWantToRead().remove(bookTitle);
        user.getCurrentlyReading().remove(bookTitle);
        user.getFinishedReading().remove(bookTitle);

        // إضافة الكتاب إلى القائمة المحددة
        switch (listType) {
            case "wanttoread":
                user.getWantToRead().add(bookTitle);
                break;
            case "currentlyreading":
                user.getCurrentlyReading().add(bookTitle);
                break;
            case "finishedreading":
                user.getFinishedReading().add(bookTitle);
                break;
            default:
                throw new RuntimeException("Invalid list type. Use: wantToRead, currentlyReading, or finishedReading");
        }

        return userRepository.save(user);
    }

    // دالة مساعدة لتوليد JWT (افتراضية)
    private String generateJwtToken(User user) {
        return "jwt-token-example";
    }
}