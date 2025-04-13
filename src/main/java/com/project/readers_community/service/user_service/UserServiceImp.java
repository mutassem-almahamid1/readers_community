package com.project.readers_community.service.user_service;

import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;
import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.Role;
import com.project.readers_community.entity.User;
import com.project.readers_community.repository.BookRepository;
import com.project.readers_community.repository.RoleRepository;
import com.project.readers_community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImp implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        
        // Assign default USER role
        if (registrationDTO.getRoles() == null || registrationDTO.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByRoleName(Role.USER_ROLE);
            if (userRole == null) {
                userRole = new Role(Role.USER_ROLE);
                roleRepository.save(userRole);
            }
            
            List<Role> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);
        } else {
            user.setRoles(registrationDTO.getRoles());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public UserLoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        
        if (user == null) {
            throw new RuntimeException("User not found with this email");
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return new UserLoginResponseDTO(user.getUsername(), user.getEmail(), user.getRoles());
    }
    
    @Override
    @Transactional
    public boolean addBookToWantToRead(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            
            // Check if book is already in user's list
            if (user.getWantToReadBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in list
            }
            
            user.getWantToReadBooks().add(book);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromWantToRead(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            boolean removed = user.getWantToReadBooks().removeIf(book -> book.getId().equals(bookId));
            
            if (removed) {
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Book> getWantToReadBooks(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getWantToReadBooks();
    }
    
    @Override
    @Transactional
    public boolean addBookToCurrentlyReading(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            
            // Check if book is already in user's list
            if (user.getCurrentlyReadingBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in list
            }
            
            user.getCurrentlyReadingBooks().add(book);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromCurrentlyReading(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            boolean removed = user.getCurrentlyReadingBooks().removeIf(book -> book.getId().equals(bookId));
            
            if (removed) {
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Book> getCurrentlyReadingBooks(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getCurrentlyReadingBooks();
    }
    
    @Override
    @Transactional
    public boolean addBookToFinishedBooks(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            
            // Check if book is already in user's list
            if (user.getFinishedBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                return false; // Book already in list
            }
            
            user.getFinishedBooks().add(book);
            
            // Add an activity record
            user.getRecentActivities().add("Finished reading: " + book.getTitle());
            
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromFinishedBooks(String userId, String bookId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            boolean removed = user.getFinishedBooks().removeIf(book -> book.getId().equals(bookId));
            
            if (removed) {
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Book> getFinishedBooks(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getFinishedBooks();
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
