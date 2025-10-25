package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.PortfolioSummaryDTO;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.model.Transaction;

import java.util.List;

public interface PortfolioService {

    // Create a new portfolio for a user
    Portfolio createPortfolio(String userId, String portfolioName, String description);

    // Update portfolio name or description
    Portfolio updatePortfolio(String portfolioId, String portfolioName, String description);

    // Retrieve a portfolio by its ID
    Portfolio getPortfolioById(String portfolioId);

    // Fetch all portfolios for a specific user
    List<Portfolio> getPortfoliosByUser(String userId);

    // Calculate total current value and P/L of a portfolio
    PortfolioSummaryDTO calculatePortfolioSummary(String portfolioId);

    // Get holdings (current assets & quantities) under a portfolio
    List<Holding> getHoldings(String portfolioId);

    // Delete a portfolio and its related transactions/holdings
    void deletePortfolio(String portfolioId);
}
