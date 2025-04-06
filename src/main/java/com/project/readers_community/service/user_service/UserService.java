package com.project.readers_community.service.user_service;

import com.project.readers_community.entity.User;

public interface UserService {
    User findByEmail(String email);
}
