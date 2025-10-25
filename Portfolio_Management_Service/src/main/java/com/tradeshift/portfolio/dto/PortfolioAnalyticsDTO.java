package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAnalyticsDTO {

    // Asset allocation by type
    private Map<String, Double> allocationByAssetType; // STOCK: 60.5, CRYPTO: 25.3, etc.
    
    // Asset allocation by individual assets
    private Map<String, Double> allocationByAsset; // AAPL: 15.2, GOOGL: 12.8, etc.
    
    // Top performing assets
    private List<AssetPerformanceDTO> topPerformers;
    
    // Worst performing assets
    private List<AssetPerformanceDTO> worstPerformers;
    
    // Diversification metrics
    private double diversificationScore; // 0-100 (higher = more diversified)
    private int totalAssets;
    private int assetTypes;
    
    // Risk metrics
    private double portfolioVolatility; // Calculated risk measure
    private String riskLevel; // LOW, MEDIUM, HIGH
    
    // Summary statistics
    private double largestPosition; // Percentage of largest holding
    private double smallestPosition; // Percentage of smallest holding
    private double averagePosition; // Average position size
}
