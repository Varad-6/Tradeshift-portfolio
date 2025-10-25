package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.PortfolioSummaryDTO;
import com.tradeshift.portfolio.exception.PortfolioNotFoundException;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Override
    public Portfolio createPortfolio(String userId, String portfolioName, String description) {

        // Check for existing portfolio with the same name for the user
        Portfolio existingPortfolio = portfolioRepository.findByUserIdAndPortfolioName(userId, portfolioName);
        if (existingPortfolio != null) {
            throw new IllegalArgumentException("Portfolio with the same name already exists for this user.");
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setPortfolioName(portfolioName);
        portfolio.setDescription(description);
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio.setHoldings(new ArrayList<>());

        return portfolioRepository.save(portfolio);
    }
    @Override
    public Portfolio updatePortfolio(String portfolioId, String portfolioName, String description) {
        // First, get the existing portfolio
        Portfolio existingPortfolio = getPortfolioById(portfolioId);

        // Check if new portfolio name already exists for this user (excluding current portfolio)
        Portfolio duplicatePortfolio = portfolioRepository.findByUserIdAndPortfolioName(
                existingPortfolio.getUserId(), portfolioName);

        if (duplicatePortfolio != null && !duplicatePortfolio.getId().equals(portfolioId)) {
            throw new IllegalArgumentException("Portfolio with the same name already exists for this user.");
        }

        // Update the fields
        existingPortfolio.setPortfolioName(portfolioName);
        existingPortfolio.setDescription(description);
        existingPortfolio.setUpdatedAt(LocalDateTime.now());

        // Save and return updated portfolio
        return portfolioRepository.save(existingPortfolio);
    }

    @Override
    public Portfolio getPortfolioById(String portfolioId) {
        // Retrieve portfolio by ID
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            throw new PortfolioNotFoundException("Portfolio not found with ID: " + portfolioId);
        }
        return portfolioOpt.get();
    }

    @Override
    public List<Portfolio> getPortfoliosByUser(String userId) {
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
        if(portfolios.isEmpty()){
            throw new PortfolioNotFoundException("No portfolios found for user ID: " + userId);
        }
        return portfolios;
    }

    @Override
    public PortfolioSummaryDTO calculatePortfolioSummary(String portfolioId) {

        // Get the portfolio with its holdings
        Portfolio portfolio = getPortfolioById(portfolioId);
        List<Holding> holdings = portfolio.getHoldings();

        // If no holdings, return zero values
        if (holdings == null || holdings.isEmpty()) {
            return PortfolioSummaryDTO.builder()
                    .totalInvestedValue(0.0)
                    .currentMarketValue(0.0)
                    .totalProfitLoss(0.0)
                    .totalChangePercentage(0.0)
                    .build();
        }
        // Calculate totals
        double totalInvestedValue = 0.0;
        double currentMarketValue = 0.0;

        for (Holding holding : holdings) {
            totalInvestedValue += holding.getInvestedValue();
            currentMarketValue += holding.getCurrentValue();
        }

        // Calculate P/L and percentage
        double totalProfitLoss = currentMarketValue - totalInvestedValue;
        double totalChangePercentage = totalInvestedValue > 0 ?
                (totalProfitLoss / totalInvestedValue) * 100 : 0.0;

        return PortfolioSummaryDTO.builder()
                .totalInvestedValue(totalInvestedValue)
                .currentMarketValue(currentMarketValue)
                .totalProfitLoss(totalProfitLoss)
                .totalChangePercentage(totalChangePercentage)
                .build();
    }

    @Override
    public List<Holding> getHoldings(String portfolioId) {
        // Get the portfolio with its holdings
        Portfolio portfolio=getPortfolioById(portfolioId);
        List<Holding> holdings=portfolio.getHoldings();

        return holdings != null ? holdings : new ArrayList<>();
    }

    @Override
    public void deletePortfolio(String portfolioId) {
        // First, check if portfolio exists
        Portfolio existingPortfolio = getPortfolioById(portfolioId);

        // If portfolio exists, delete it
        portfolioRepository.deleteById(portfolioId);
    }
}
