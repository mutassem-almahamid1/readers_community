package com.project.readers_community.service.user_service;

import com.project.readers_community.dto.user_dto.UserRegistrationDTO;
import com.project.readers_community.dto.user_dto.UserLoginDTO;
import com.project.readers_community.dto.user_dto.UserLoginResponseDTO;
import com.project.readers_community.entity.User;
import com.project.readers_community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImp {

    @Autowired
    private UserRepository userRepository;


    // تسجيل مستخدم جديد
    public User registerUser(UserRegistrationDTO registrationDTO) {

        //  الأيميل موجود مسبقًا
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        else{
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
        user.setRole(registrationDTO.getRole());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
        }
    }

    // تسجيل الدخول
    public UserLoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null) {

            if (loginDTO.getPassword().equals(user.getPassword())) {
                return new UserLoginResponseDTO(user.getUsername(), user.getEmail(),user.getRole());
            } else {
                throw new RuntimeException("Invalid email or password");
            }

        }
        else{
            throw new RuntimeException("Invalid email or password");
            }
    }




}

