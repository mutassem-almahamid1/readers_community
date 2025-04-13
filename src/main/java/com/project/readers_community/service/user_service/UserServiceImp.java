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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Check if email already exists
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }
        
        // Check if username already exists
        if (userRepository.findByUsername(registrationDTO.getUsername()) != null) {
            throw new RuntimeException("Username already in use");
        }
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        
        // Initialize collections
        user.setRoles(new ArrayList<>());
        user.setCreatedAt(LocalDateTime.now());
        user.setFollowers(new ArrayList<>());
        user.setFollowing(new ArrayList<>());
        user.setRecentActivities(new ArrayList<>());
        user.setReviews(new ArrayList<>());
        
        // Save the user first to get an ID
        user = userRepository.save(user);
        
        // Check if roles are provided, if not add default USER role
        List<Role> roles = registrationDTO.getRoles();
        if (roles == null || roles.isEmpty()) {
            Role userRole = new Role(Role.USER_ROLE, user);
            roleRepository.save(userRole);
            user.addRole(userRole);
        } else {
            for (Role role : roles) {
                role.addUser(user);
                roleRepository.save(role);
                user.addRole(role);
            }
        }
        
        // Save user again with updated roles
        return userRepository.save(user);
    }
    
    @Override
    public UserLoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        
        if (user == null) {
            throw new RuntimeException("User not found");
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
            User user = getUserById(userId);
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                
                // Check if book already exists in the list
                if (!user.getWantToReadBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                    user.getWantToReadBooks().add(book);
                    user.getRecentActivities().add("Added book \"" + book.getTitle() + "\" to Want to Read list");
                    userRepository.save(user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromWantToRead(String userId, String bookId) {
        try {
            User user = getUserById(userId);
            
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
        User user = getUserById(userId);
        return user.getWantToReadBooks();
    }
    
    @Override
    @Transactional
    public boolean addBookToCurrentlyReading(String userId, String bookId) {
        try {
            User user = getUserById(userId);
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                
                // Check if book already exists in the list
                if (!user.getCurrentlyReadingBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                    user.getCurrentlyReadingBooks().add(book);
                    user.getRecentActivities().add("Started reading \"" + book.getTitle() + "\"");
                    userRepository.save(user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromCurrentlyReading(String userId, String bookId) {
        try {
            User user = getUserById(userId);
            
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
        User user = getUserById(userId);
        return user.getCurrentlyReadingBooks();
    }
    
    @Override
    @Transactional
    public boolean addBookToFinishedBooks(String userId, String bookId) {
        try {
            User user = getUserById(userId);
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                
                // Check if book already exists in the list
                if (!user.getFinishedBooks().stream().anyMatch(b -> b.getId().equals(bookId))) {
                    user.getFinishedBooks().add(book);
                    user.getRecentActivities().add("Finished reading \"" + book.getTitle() + "\"");
                    
                    // Remove from currently reading if present
                    user.getCurrentlyReadingBooks().removeIf(b -> b.getId().equals(bookId));
                    
                    userRepository.save(user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeBookFromFinishedBooks(String userId, String bookId) {
        try {
            User user = getUserById(userId);
            
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
        User user = getUserById(userId);
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
