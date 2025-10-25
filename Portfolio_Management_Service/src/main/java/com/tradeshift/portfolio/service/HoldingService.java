package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.model.Holding;

import java.util.List;

public interface HoldingService {
    // Get all holdings in a portfolio - MOST USED
    List<Holding> getHoldingsByPortfolio(String portfolioId);

    // Get specific holding by asset symbol - COMMONLY USED
    Holding getHoldingByAsset(String portfolioId, String assetSymbol);

    // Add new holding to portfolio - INTERNAL USE
    Holding addHolding(String portfolioId, Holding holding);

    // Update holding details - INTERNAL USE
    Holding updateHolding(String portfolioId, String assetSymbol, Holding updatedHolding);

    // Remove holding from portfolio - INTERNAL USE
    void removeHolding(String portfolioId, String assetSymbol);

    // Update holding prices - SYSTEM USE
    void updateHoldingPrices(String portfolioId, String assetSymbol, double currentPrice);
}
