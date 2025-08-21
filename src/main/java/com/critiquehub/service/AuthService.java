package com.critiquehub.service;

import com.critiquehub.dto.request.LoginRequest;
import com.critiquehub.dto.request.RegisterRequest;
import com.critiquehub.dto.response.JwtAuthResponse;
import com.critiquehub.dto.response.UserResponse;
import com.critiquehub.entity.User;
import com.critiquehub.exception.BadRequestException;
import com.critiquehub.repository.UserRepository;
import com.critiquehub.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public JwtAuthResponse registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Set default role to READER if not provided
        if (signUpRequest.getRole() == null) {
            user.setRole(User.Role.READER);
        } else {
            user.setRole(signUpRequest.getRole());
        }

        user.setEnabled(true);

        User result = userRepository.save(user);

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserResponse userResponse = new UserResponse(
                result.getId(),
                result.getUsername(),
                result.getEmail(),
                result.getRole(),
                result.getCreatedAt()
        );

        return new JwtAuthResponse(jwt, userResponse);
    }

    @Transactional
    public JwtAuthResponse registerAdmin(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        // Create new admin user
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(User.Role.ADMIN); // Force ADMIN role
        user.setEnabled(true);

        User result = userRepository.save(user);

        // Authenticate admin
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserResponse userResponse = new UserResponse(
                result.getId(),
                result.getUsername(),
                result.getEmail(),
                result.getRole(),
                result.getCreatedAt()
        );

        return new JwtAuthResponse(jwt, userResponse);
    }

    public boolean adminExists() {
        return userRepository.existsByRole(User.Role.ADMIN);
    }

    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );

        return new JwtAuthResponse(jwt, userResponse);
    }

    // Optional: Method to check if user exists by username
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Optional: Method to check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}