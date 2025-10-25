package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.UserProfileDTO;
import com.tradeshift.portfolio.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnhancedJwtService {

    private final JwtUtil jwtUtil;
    private final AuthServiceClient authServiceClient;

    /**
     * Enhanced JWT validation - validates locally and optionally with Auth Service
     * @param token JWT token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        // First validate locally (faster)
        if (!jwtUtil.validateToken(token)) {
            return false;
        }

        // Optional: Also validate with Auth Service for additional security
        // Uncomment the line below if you want to validate with Auth Service
        // return authServiceClient.validateTokenWithAuthService(token);
        
        return true; // Local validation passed
    }

    /**
     * Get user email from JWT token
     * @param token JWT token
     * @return user email or null if invalid
     */
    public String getEmailFromToken(String token) {
        if (!validateToken(token)) {
            return null;
        }
        return jwtUtil.getEmailFromToken(token);
    }

    /**
     * Get user authorities from JWT token
     * @param token JWT token
     * @return user authorities or null if invalid
     */
    public String getAuthoritiesFromToken(String token) {
        if (!validateToken(token)) {
            return null;
        }
        return jwtUtil.getAuthoritiesFromToken(token);
    }

    /**
     * Check if user has specific role
     * @param token JWT token
     * @param role role to check
     * @return true if user has role, false otherwise
     */
    public boolean hasRole(String token, String role) {
        if (!validateToken(token)) {
            return false;
        }
        return jwtUtil.hasRole(token, role);
    }

    /**
     * Get full user profile from Auth Service
     * @param token JWT token
     * @return user profile or null if invalid
     */
    public UserProfileDTO getUserProfile(String token) {
        if (!validateToken(token)) {
            return null;
        }
        return authServiceClient.getUserProfile(token);
    }
}
