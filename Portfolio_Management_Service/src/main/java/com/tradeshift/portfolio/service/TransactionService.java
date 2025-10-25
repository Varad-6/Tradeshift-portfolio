package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.TransactionSummaryDTO;
import com.tradeshift.portfolio.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    // Record transaction AND update portfolio
    Transaction addTransaction(String portfolioId, Transaction transaction);

    // Get transactions for specific portfolio
    List<Transaction> getTransactionsByPortfolio(String portfolioId);

    // Get user's all transactions
    List<Transaction> getTransactionsByUser(String userId);

    // Get asset-specific transactions
    List<Transaction> getTransactionsByAsset(String userId, String assetSymbol);

    // Get date range transactions
    List<Transaction> getTransactionsByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    // Recalculate portfolio after transaction
    void recalculatePortfolio(String portfolioId);

    // Get transaction summary
    TransactionSummaryDTO getTransactionSummary(String userId);
}
