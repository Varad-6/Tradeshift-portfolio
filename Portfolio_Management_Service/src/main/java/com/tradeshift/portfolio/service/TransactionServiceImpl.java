package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.TransactionSummaryDTO;
import com.tradeshift.portfolio.exception.InvalidTransactionException;
import com.tradeshift.portfolio.exception.PortfolioNotFoundException;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.model.Transaction;
import com.tradeshift.portfolio.repository.PortfolioRepository;
import com.tradeshift.portfolio.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public Transaction addTransaction(String portfolioId, Transaction transaction) {
        // Get the portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with ID: " + portfolioId));

        // Validate transaction
        if (transaction.getQuantity() <= 0) {
            throw new InvalidTransactionException("Quantity must be greater than 0");
        }
        if (transaction.getPricePerUnit() <= 0) {
            throw new InvalidTransactionException("Price per unit must be greater than 0");
        }
        if (!"BUY".equals(transaction.getTransactionType()) && !"SELL".equals(transaction.getTransactionType())) {
            throw new InvalidTransactionException("Transaction type must be BUY or SELL");
        }

        // Set transaction details
        transaction.setUserId(portfolio.getUserId());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTotalValue(transaction.getQuantity() * transaction.getPricePerUnit());

        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Update portfolio holdings
        updatePortfolioHoldings(portfolio, transaction);

        // Recalculate portfolio
        recalculatePortfolio(portfolioId);

        return savedTransaction;
    }

    private void updatePortfolioHoldings(Portfolio portfolio, Transaction transaction) {
        List<Holding> holdings = portfolio.getHoldings();
        if (holdings == null) {
            holdings = new ArrayList<>();
        }

        // Find existing holding for this asset
        Optional<Holding> existingHolding = holdings.stream()
                .filter(h -> h.getAssetSymbol().equals(transaction.getAssetSymbol()))
                .findFirst();

        if (existingHolding.isPresent()) {
            // Update existing holding
            Holding holding = existingHolding.get();
            if ("BUY".equals(transaction.getTransactionType())) {
                // Buy: Add to existing position
                double newQuantity = holding.getQuantity() + transaction.getQuantity();
                double newAvgPrice = ((holding.getQuantity() * holding.getAvgBuyPrice()) +
                        (transaction.getQuantity() * transaction.getPricePerUnit())) / newQuantity;

                holding.setQuantity(newQuantity);
                holding.setAvgBuyPrice(newAvgPrice);
            } else {
                // Sell: Reduce position
                double newQuantity = holding.getQuantity() - transaction.getQuantity();
                if (newQuantity < 0) {
                    throw new InvalidTransactionException("Insufficient quantity to sell");
                }
                holding.setQuantity(newQuantity);
            }

            // Update current values
            holding.setCurrentPrice(transaction.getPricePerUnit());
            holding.setCurrentValue(holding.getQuantity() * holding.getCurrentPrice());
            holding.setInvestedValue(holding.getQuantity() * holding.getAvgBuyPrice());
            holding.setProfitLoss(holding.getCurrentValue() - holding.getInvestedValue());
            holding.setChangePercentage(holding.getInvestedValue() > 0 ?
                    (holding.getProfitLoss() / holding.getInvestedValue()) * 100 : 0.0);
            holding.setLastUpdated(LocalDateTime.now());
        } else {
            // Create new holding
            if ("BUY".equals(transaction.getTransactionType())) {
                Holding newHolding = Holding.builder()
                        .assetSymbol(transaction.getAssetSymbol())
                        .assetName(transaction.getAssetName())
                        .assetType(transaction.getAssetType())
                        .quantity(transaction.getQuantity())
                        .avgBuyPrice(transaction.getPricePerUnit())
                        .currentPrice(transaction.getPricePerUnit())
                        .currentValue(transaction.getQuantity() * transaction.getPricePerUnit())
                        .investedValue(transaction.getQuantity() * transaction.getPricePerUnit())
                        .profitLoss(0.0)
                        .changePercentage(0.0)
                        .lastUpdated(LocalDateTime.now())
                        .build();

                holdings.add(newHolding);
            } else {
                throw new InvalidTransactionException("Cannot sell asset that you don't own");
            }
        }

        // Update portfolio
        portfolio.setHoldings(holdings);
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);
    }



    @Override
    public List<Transaction> getTransactionsByPortfolio(String portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("Portfolio not found with ID: " + portfolioId));

        List<Transaction> userTransactions = transactionRepository.findByUserId(portfolio.getUserId());

        return userTransactions;
    }

    @Override
    public List<Transaction> getTransactionsByUser(String userId) {
        List<Transaction> transactions=transactionRepository.findByUserId(userId);
        return transactions != null ? transactions : new ArrayList<>();
    }

    @Override
    public List<Transaction> getTransactionsByAsset(String userId, String assetSymbol) {

        List<Transaction> transactions =
                transactionRepository.findByUserIdAndAssetSymbol(userId, assetSymbol);
        return transactions != null ? transactions : new ArrayList<>();
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(
            String userId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        List<Transaction> transactions = transactionRepository.
                findByUserIdAndTransactionDateBetween(userId, startDate, endDate);

        return transactions != null ? transactions : new ArrayList<>();
    }

    @Override
    public void recalculatePortfolio(String portfolioId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Portfolio not found with ID: "
                                + portfolioId));
        List<Holding> holdings = portfolio.getHoldings();
        if (holdings == null || holdings.isEmpty()) {
            return; // Nothing to recalculate
        }
        for (Holding holding : holdings) {
            // Recalculate current value
            holding.setCurrentValue(holding.getQuantity() * holding.getCurrentPrice());
            // Recalculate invested value
            holding.setInvestedValue(holding.getQuantity() * holding.getAvgBuyPrice());
            // Recalculate profit/loss
            holding.setProfitLoss(holding.getCurrentValue() - holding.getInvestedValue());
            // Recalculate change percentage
            if (holding.getInvestedValue() > 0) {
                holding.setChangePercentage((holding.getProfitLoss() / holding.getInvestedValue()) * 100);
            } else {
                holding.setChangePercentage(0.0);
            }
            // Update timestamp
            holding.setLastUpdated(LocalDateTime.now());
        }
        // Update portfolio timestamp
        portfolio.setUpdatedAt(LocalDateTime.now());
        // Save updated portfolio
        portfolioRepository.save(portfolio);
    }

    @Override
    public TransactionSummaryDTO getTransactionSummary(String userId) {
        // Get all transactions for this user
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        if (transactions.isEmpty()) {
            return TransactionSummaryDTO.builder()
                    .totalTransactions(0)
                    .buyTransactions(0)
                    .sellTransactions(0)
                    .totalBuyValue(0.0)
                    .totalSellValue(0.0)
                    .netTransactionValue(0.0)
                    .totalFees(0.0)
                    .mostTradedAsset("N/A")
                    .mostTradedAssetCount(0)
                    .build();
        }

        // Calculate summary statistics
        int totalTransactions = transactions.size();
        int buyTransactions = (int) transactions.stream()
                .filter(t -> "BUY".equals(t.getTransactionType()))
                .count();
        int sellTransactions = totalTransactions - buyTransactions;

        double totalBuyValue = transactions.stream()
                .filter(t -> "BUY".equals(t.getTransactionType()))
                .mapToDouble(Transaction::getTotalValue)
                .sum();

        double totalSellValue = transactions.stream()
                .filter(t -> "SELL".equals(t.getTransactionType()))
                .mapToDouble(Transaction::getTotalValue)
                .sum();

        double netTransactionValue = totalSellValue - totalBuyValue;
        double totalFees = transactions.stream()
                .mapToDouble(Transaction::getFees)
                .sum();

        // Find most traded asset
        Map<String, Long> assetCounts = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAssetSymbol, Collectors.counting()));

        Map.Entry<String, Long> mostTraded = assetCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(Map.entry("N/A", 0L));

        return TransactionSummaryDTO.builder()
                .totalTransactions(totalTransactions)
                .buyTransactions(buyTransactions)
                .sellTransactions(sellTransactions)
                .totalBuyValue(totalBuyValue)
                .totalSellValue(totalSellValue)
                .netTransactionValue(netTransactionValue)
                .totalFees(totalFees)
                .mostTradedAsset(mostTraded.getKey())
                .mostTradedAssetCount(mostTraded.getValue().intValue())
                .build();
    }


}
