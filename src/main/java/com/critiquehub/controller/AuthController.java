package com.critiquehub.controller;

import com.critiquehub.dto.request.LoginRequest;
import com.critiquehub.dto.request.RegisterRequest;
import com.critiquehub.dto.response.JwtAuthResponse;
import com.critiquehub.entity.User;
import com.critiquehub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

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

    @PostMapping("/register-admin")
    @Operation(summary = "Admin Registration", description = "Register new admin user (for development)")
    @PreAuthorize("hasRole('ADMIN')") // Only existing admins can create new admins
    public ResponseEntity<JwtAuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest signUpRequest) {
        JwtAuthResponse response = authService.registerAdmin(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Optional: Public admin registration for initial setup (remove after first admin is created)
    @PostMapping("/setup-admin")
    @Operation(summary = "Initial Admin Setup", description = "Create first admin user (for initial setup only)")
    public ResponseEntity<JwtAuthResponse> setupAdmin(@Valid @RequestBody RegisterRequest signUpRequest) {
        // Check if any admin already exists
        if (authService.adminExists()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new JwtAuthResponse(false, "Admin setup already completed", null, null, null));
        }

        JwtAuthResponse response = authService.registerAdmin(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}