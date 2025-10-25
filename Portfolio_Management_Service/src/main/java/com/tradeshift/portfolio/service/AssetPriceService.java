package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.model.AssetPrice;

import java.util.List;

public interface AssetPriceService {

    // Get current price for a specific asset
    AssetPrice getCurrentPrice(String assetSymbol);

    // Get current prices for multiple assets
    List<AssetPrice> getCurrentPrices(List<String> assetSymbols);

    // Update price for a specific asset (for real-time updates)
    AssetPrice updateAssetPrice(String assetSymbol, double newPrice);

    // Update prices for multiple assets (batch update)
    List<AssetPrice> updateAssetPrices(List<String> assetSymbols, List<Double> newPrices);

    // Get all assets of a specific type (STOCK, CRYPTO, etc.)
    List<AssetPrice> getAssetsByType(String assetType);

    // Refresh all portfolio holdings with latest prices
    void refreshPortfolioPrices(String portfolioId);

    // Get price history for an asset (if we implement it later)
    // List<AssetPrice> getPriceHistory(String assetSymbol, int days);
}
