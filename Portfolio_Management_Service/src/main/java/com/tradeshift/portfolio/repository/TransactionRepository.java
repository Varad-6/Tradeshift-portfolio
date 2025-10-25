package com.tradeshift.portfolio.repository;

import com.tradeshift.portfolio.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // Find all transactions for a given user
    List<Transaction> findByUserId(String userId);

    // Get all transactions for a user and a specific asset
    List<Transaction> findByUserIdAndAssetSymbol(String userId, String assetSymbol);

    // Optional: Get all transactions for a user within a date range
    List<Transaction> findByUserIdAndTransactionDateBetween(String userId,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate);
}
