package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    /**
     * Get user profile from Authentication Service
     * @param jwt JWT token from request
     * @return User profile information
     */
    public UserProfileDTO getUserProfile(String jwt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = authServiceUrl + "/auth/api/user/profile";
            ResponseEntity<UserProfileDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserProfileDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.out.println("Error calling Auth Service: " + e.getMessage());
        }
        return null;
    }

    /**
     * Validate JWT token with Authentication Service
     * @param jwt JWT token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateTokenWithAuthService(String jwt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = authServiceUrl + "/auth/api/user/profile";
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.out.println("Error validating token with Auth Service: " + e.getMessage());
            return false;
        }
    }
}
