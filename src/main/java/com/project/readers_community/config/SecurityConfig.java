package com.project.readers_community.config;

import com.project.readers_community.handelException.exception.BadCredentialsException;
import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.security.JwtDatabaseAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDatabaseAuthenticationFilter jwtDatabaseAuthenticationFilter;

    private static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/book/title").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler())
                                .authenticationEntryPoint(errorAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtDatabaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN,
                        String.format("Access Denied: [%s] You do not have permission to access this resource.", request.getUserPrincipal().getName()));
    }

    public AuthenticationEntryPoint errorAuthenticationEntryPoint() {
        return (request, response, authException) ->
                writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, authException.getMessage());
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"" + message + "\"}");
        }catch (Exception e){
            throw new BadReqException("Failed to write JSON response, msg: " + e.getMessage());
        }
    }

}