package com.project.readers_community.security;

import com.project.readers_community.handelException.exception.BadCredentialsException;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService implementation for Spring Security
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Try to find user by username or email
        User user = userRepository.getByEmail(usernameOrEmail);

        if (user.getStatus() == Status.BLOCKED) {
            throw new BadCredentialsException("Your account is blocked. Connect with support.");
        }

        // Map roles to authorities
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Return Spring Security UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
