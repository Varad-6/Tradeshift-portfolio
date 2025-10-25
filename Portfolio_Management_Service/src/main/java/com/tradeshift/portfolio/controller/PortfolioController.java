package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.dto.PortfolioSummaryDTO;
import com.tradeshift.portfolio.dto.UpdatePortfolioDTO;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.security.JwtUtil;
import com.tradeshift.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<Portfolio> createPortfolio(@RequestHeader("Authorization") String jwt,
                                                     @RequestParam String portfolioName,
                                                     @RequestParam String description) {
        try{
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Get user email from JWT token
            String userEmail = jwtUtil.getEmailFromToken(jwt);
            if (userEmail == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Use email as userId (you can modify this based on your user ID strategy)
            Portfolio portfolio = portfolioService.createPortfolio(userEmail, portfolioName, description);
            return new ResponseEntity<>(portfolio, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable String portfolioId,
                                                     @RequestBody UpdatePortfolioDTO request) {
        try {
            Portfolio updatedPortfolio = portfolioService.updatePortfolio(
                    portfolioId,
                    request.getPortfolioName(),
                    request.getDescription()
            );
            return new ResponseEntity<>(updatedPortfolio, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable String portfolioId) {
        try {
            Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
            return new ResponseEntity<>(portfolio, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Portfolio>> getPortfoliosByUser(@RequestHeader("Authorization") String jwt) {
        try{
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Get user email from JWT token
            String userEmail = jwtUtil.getEmailFromToken(jwt);
            if (userEmail == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            List<Portfolio> portfolios = portfolioService.getPortfoliosByUser(userEmail);
            return new ResponseEntity<>(portfolios, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{portfolioId}/summary")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary(@PathVariable String portfolioId) {
        try{
            PortfolioSummaryDTO summary = portfolioService.calculatePortfolioSummary(portfolioId);
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{portfolioId}/holdings")
    public ResponseEntity<List<Holding>> getHoldings(@PathVariable String portfolioId) {
        try {
            List<Holding> holdings = portfolioService.getHoldings(portfolioId);
            return new ResponseEntity<>(holdings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable String portfolioId) {
        try {
            portfolioService.deletePortfolio(portfolioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
