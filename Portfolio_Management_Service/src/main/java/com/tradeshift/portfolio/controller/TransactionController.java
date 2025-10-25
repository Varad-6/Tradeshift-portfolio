package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.dto.TransactionDTO;
import com.tradeshift.portfolio.dto.TransactionSummaryDTO;
import com.tradeshift.portfolio.model.Transaction;
import com.tradeshift.portfolio.security.JwtUtil;
import com.tradeshift.portfolio.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/portfolio/{portfolioId}")
    public ResponseEntity<Transaction> addTransaction(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable String portfolioId,
                                                      @RequestBody TransactionDTO transactionDTO) {
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
            
            // Convert DTO to Entity
            Transaction transaction = Transaction.builder()
                    .assetSymbol(transactionDTO.getAssetSymbol())
                    .assetName(transactionDTO.getAssetName())
                    .assetType(transactionDTO.getAssetType())
                    .transactionType(transactionDTO.getTransactionType())
                    .quantity(transactionDTO.getQuantity())
                    .pricePerUnit(transactionDTO.getPricePerUnit())
                    .fees(transactionDTO.getFees())
                    .remarks(transactionDTO.getRemarks())
                    .build();

            Transaction savedTransaction = transactionService.addTransaction(portfolioId, transaction);
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Transaction>> getTransactionsByPortfolio(@PathVariable String portfolioId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByPortfolio(portfolioId);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@RequestHeader("Authorization") String jwt) {
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
            
            List<Transaction> transactions = transactionService.getTransactionsByUser(userEmail);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}/asset/{assetSymbol}")
    public ResponseEntity<List<Transaction>> getTransactionsByAsset(@PathVariable String userId,
                                                                    @PathVariable String assetSymbol) {
        List<Transaction> transactions = transactionService.getTransactionsByAsset(userId, assetSymbol);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(@PathVariable String userId,
                                                                        @RequestParam String startDate,
                                                                        @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Transaction> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/summary")
    public ResponseEntity<TransactionSummaryDTO> getTransactionSummary(@RequestHeader("Authorization") String jwt) {
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
            
            TransactionSummaryDTO summary = transactionService.getTransactionSummary(userEmail);
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/portfolio/{portfolioId}/recalculate")
    public ResponseEntity<String> recalculatePortfolio(@PathVariable String portfolioId) {
        try {
            transactionService.recalculatePortfolio(portfolioId);
            return new ResponseEntity<>("Portfolio recalculated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Portfolio not found with ID: " + portfolioId, HttpStatus.NOT_FOUND);
        }
    }
}
