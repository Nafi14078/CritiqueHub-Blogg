package com.critiquehub.dto.response;

import java.time.LocalDateTime;

public class JwtAuthResponse {

    private boolean success;
    private String message;
    private String accessToken;
    private String tokenType = "Bearer";
    private LocalDateTime expiresAt;
    private UserResponse user;

    // Constructors
    public JwtAuthResponse() {}

    // For successful responses
    public JwtAuthResponse(String accessToken, UserResponse user) {
        this.success = true;
        this.message = "Authentication successful";
        this.accessToken = accessToken;
        this.user = user;
        this.tokenType = "Bearer";
    }

    public JwtAuthResponse(String accessToken, LocalDateTime expiresAt, UserResponse user) {
        this.success = true;
        this.message = "Authentication successful";
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.user = user;
        this.tokenType = "Bearer";
    }

    // For error responses
    public JwtAuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.accessToken = null;
        this.user = null;
    }

    public JwtAuthResponse(boolean b, String adminSetupAlreadyCompleted, Object o, Object o1, Object o2) {
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "JwtAuthResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", accessToken='" + (accessToken != null ? "***" : null) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresAt=" + expiresAt +
                ", user=" + user +
                '}';
    }
}