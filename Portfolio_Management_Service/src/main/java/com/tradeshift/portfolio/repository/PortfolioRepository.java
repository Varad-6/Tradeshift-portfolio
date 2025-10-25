package com.tradeshift.portfolio.repository;

import com.tradeshift.portfolio.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

    // Find all portfolios for a given user
    List<Portfolio> findByUserId(String userId);

    // Find a portfolio by user ID and portfolio name
    Portfolio findByUserIdAndPortfolioName(String userId, String portfolioName);
}
