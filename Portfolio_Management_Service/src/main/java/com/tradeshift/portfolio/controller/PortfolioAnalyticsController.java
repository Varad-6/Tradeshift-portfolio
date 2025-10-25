package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.dto.PortfolioAnalyticsDTO;
import com.tradeshift.portfolio.security.JwtUtil;
import com.tradeshift.portfolio.service.PortfolioAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class PortfolioAnalyticsController {

    private final PortfolioAnalyticsService portfolioAnalyticsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<PortfolioAnalyticsDTO> getPortfolioAnalytics(@RequestHeader("Authorization") String jwt,
                                                                        @PathVariable String portfolioId) {
        try {
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Get user email from JWT token
            String userEmail = jwtUtil.getEmailFromToken(jwt);
            if (userEmail == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            PortfolioAnalyticsDTO analytics = portfolioAnalyticsService.getPortfolioAnalytics(portfolioId);
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
