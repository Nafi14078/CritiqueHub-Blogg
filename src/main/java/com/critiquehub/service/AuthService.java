package com.critiquehub.service;

import com.critiquehub.dto.request.LoginRequest;
import com.critiquehub.dto.request.RegisterRequest;
import com.critiquehub.dto.response.JwtAuthResponse;
import com.critiquehub.dto.response.UserResponse;
import com.critiquehub.entity.User;
import com.critiquehub.exception.BadRequestException;
import com.critiquehub.repository.UserRepository;
import com.critiquehub.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
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
        user.setRole(signUpRequest.getRole());
        
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
}

