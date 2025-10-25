package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.dto.PortfolioAnalyticsDTO;

public interface PortfolioAnalyticsService {

    // Get comprehensive portfolio analytics
    PortfolioAnalyticsDTO getPortfolioAnalytics(String portfolioId);

    // Get asset allocation by type (STOCK, CRYPTO, etc.)
    // Map<String, Double> getAllocationByAssetType(String portfolioId);

    // Get asset allocation by individual assets
    // Map<String, Double> getAllocationByAsset(String portfolioId);

    // Get top performing assets
    // List<AssetPerformanceDTO> getTopPerformers(String portfolioId, int limit);

    // Get worst performing assets
    // List<AssetPerformanceDTO> getWorstPerformers(String portfolioId, int limit);

    // Calculate diversification score (0-100)
    // double calculateDiversificationScore(String portfolioId);

    // Get risk assessment
    // String getRiskLevel(String portfolioId);
}
