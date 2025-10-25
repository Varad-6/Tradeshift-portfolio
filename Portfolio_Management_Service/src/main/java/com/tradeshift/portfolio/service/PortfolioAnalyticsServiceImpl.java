package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.AssetPerformanceDTO;
import com.tradeshift.portfolio.dto.PortfolioAnalyticsDTO;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioAnalyticsServiceImpl implements PortfolioAnalyticsService {

    private final PortfolioRepository portfolioRepository;

    @Override
    public PortfolioAnalyticsDTO getPortfolioAnalytics(String portfolioId) {
        // Step 1: Get the portfolio with holdings
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));

        List<Holding> holdings = portfolio.getHoldings();
        
        // Step 2: Check if portfolio has holdings
        if (holdings == null || holdings.isEmpty()) {
            return createEmptyAnalytics();
        }

        // Step 3: Calculate total portfolio value
        double totalPortfolioValue = holdings.stream()
                .mapToDouble(Holding::getCurrentValue)
                .sum();

        // Step 4: Create asset performance list
        List<AssetPerformanceDTO> assetPerformances = createAssetPerformances(holdings, totalPortfolioValue);

        // Step 5: Calculate allocations
        Map<String, Double> allocationByAssetType = calculateAllocationByAssetType(holdings, totalPortfolioValue);
        Map<String, Double> allocationByAsset = calculateAllocationByAsset(holdings, totalPortfolioValue);

        // Step 6: Get top and worst performers
        List<AssetPerformanceDTO> topPerformers = getTopPerformers(assetPerformances, 5);
        List<AssetPerformanceDTO> worstPerformers = getWorstPerformers(assetPerformances, 5);

        // Step 7: Calculate diversification metrics
        double diversificationScore = calculateDiversificationScore(holdings);
        int totalAssets = holdings.size();
        int assetTypes = (int) holdings.stream()
                .map(Holding::getAssetType)
                .distinct()
                .count();

        // Step 8: Calculate risk metrics
        double portfolioVolatility = calculatePortfolioVolatility(holdings);
        String riskLevel = determineRiskLevel(portfolioVolatility, diversificationScore);

        // Step 9: Calculate position statistics
        double largestPosition = allocationByAsset.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double smallestPosition = allocationByAsset.values().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double averagePosition = allocationByAsset.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Step 10: Build and return analytics DTO
        return PortfolioAnalyticsDTO.builder()
                .allocationByAssetType(allocationByAssetType)
                .allocationByAsset(allocationByAsset)
                .topPerformers(topPerformers)
                .worstPerformers(worstPerformers)
                .diversificationScore(diversificationScore)
                .totalAssets(totalAssets)
                .assetTypes(assetTypes)
                .portfolioVolatility(portfolioVolatility)
                .riskLevel(riskLevel)
                .largestPosition(largestPosition)
                .smallestPosition(smallestPosition)
                .averagePosition(averagePosition)
                .build();
    }

    private PortfolioAnalyticsDTO createEmptyAnalytics() {
        return PortfolioAnalyticsDTO.builder()
                .allocationByAssetType(new HashMap<>())
                .allocationByAsset(new HashMap<>())
                .topPerformers(new ArrayList<>())
                .worstPerformers(new ArrayList<>())
                .diversificationScore(0.0)
                .totalAssets(0)
                .assetTypes(0)
                .portfolioVolatility(0.0)
                .riskLevel("N/A")
                .largestPosition(0.0)
                .smallestPosition(0.0)
                .averagePosition(0.0)
                .build();
    }

    private List<AssetPerformanceDTO> createAssetPerformances(List<Holding> holdings, double totalPortfolioValue) {
        List<AssetPerformanceDTO> performances = new ArrayList<>();
        
        for (Holding holding : holdings) {
            double portfolioAllocation = totalPortfolioValue > 0 ? 
                (holding.getCurrentValue() / totalPortfolioValue) * 100 : 0.0;
            
            AssetPerformanceDTO performance = AssetPerformanceDTO.builder()
                    .assetSymbol(holding.getAssetSymbol())
                    .assetName(holding.getAssetName())
                    .assetType(holding.getAssetType())
                    .currentValue(holding.getCurrentValue())
                    .investedValue(holding.getInvestedValue())
                    .profitLoss(holding.getProfitLoss())
                    .changePercentage(holding.getChangePercentage())
                    .portfolioAllocation(portfolioAllocation)
                    .rank(0) // Will be set later
                    .build();
            
            performances.add(performance);
        }
        
        return performances;
    }

    private Map<String, Double> calculateAllocationByAssetType(List<Holding> holdings, double totalPortfolioValue) {
        Map<String, Double> allocationByType = new HashMap<>();
        
        for (Holding holding : holdings) {
            String assetType = holding.getAssetType();
            double currentAllocation = allocationByType.getOrDefault(assetType, 0.0);
            double holdingAllocation = totalPortfolioValue > 0 ? 
                (holding.getCurrentValue() / totalPortfolioValue) * 100 : 0.0;
            allocationByType.put(assetType, currentAllocation + holdingAllocation);
        }
        
        return allocationByType;
    }

    private Map<String, Double> calculateAllocationByAsset(List<Holding> holdings, double totalPortfolioValue) {
        Map<String, Double> allocationByAsset = new HashMap<>();
        
        for (Holding holding : holdings) {
            double allocation = totalPortfolioValue > 0 ? 
                (holding.getCurrentValue() / totalPortfolioValue) * 100 : 0.0;
            allocationByAsset.put(holding.getAssetSymbol(), allocation);
        }
        
        return allocationByAsset;
    }

    private List<AssetPerformanceDTO> getTopPerformers(List<AssetPerformanceDTO> performances, int limit) {
        return performances.stream()
                .sorted((a, b) -> Double.compare(b.getChangePercentage(), a.getChangePercentage()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<AssetPerformanceDTO> getWorstPerformers(List<AssetPerformanceDTO> performances, int limit) {
        return performances.stream()
                .sorted((a, b) -> Double.compare(a.getChangePercentage(), b.getChangePercentage()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double calculateDiversificationScore(List<Holding> holdings) {
        if (holdings.size() <= 1) return 0.0;
        
        // Simple diversification score based on number of assets and types
        int totalAssets = holdings.size();
        int assetTypes = (int) holdings.stream().map(Holding::getAssetType).distinct().count();
        
        // Score based on asset count (max 50 points) and type diversity (max 50 points)
        double assetScore = Math.min(50.0, totalAssets * 5.0);
        double typeScore = Math.min(50.0, assetTypes * 16.67);
        
        return assetScore + typeScore;
    }

    private double calculatePortfolioVolatility(List<Holding> holdings) {
        // Simplified volatility calculation based on position sizes
        if (holdings.isEmpty()) return 0.0;
        
        double totalValue = holdings.stream().mapToDouble(Holding::getCurrentValue).sum();
        if (totalValue == 0) return 0.0;
        
        // Calculate variance of position sizes
        double mean = 100.0 / holdings.size(); // Equal allocation percentage
        double variance = holdings.stream()
                .mapToDouble(h -> Math.pow((h.getCurrentValue() / totalValue) * 100 - mean, 2))
                .average()
                .orElse(0.0);
        
        return Math.sqrt(variance);
    }

    private String determineRiskLevel(double volatility, double diversificationScore) {
        if (diversificationScore >= 80 && volatility <= 10) return "LOW";
        if (diversificationScore >= 60 && volatility <= 20) return "MEDIUM";
        return "HIGH";
    }
}
