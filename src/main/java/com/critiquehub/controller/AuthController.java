package com.critiquehub.controller;

import com.critiquehub.dto.request.LoginRequest;
import com.critiquehub.dto.request.RegisterRequest;
import com.critiquehub.dto.response.JwtAuthResponse;
import com.critiquehub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT token")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Register new user and return JWT token")
    public ResponseEntity<JwtAuthResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        JwtAuthResponse response = authService.registerUser(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
