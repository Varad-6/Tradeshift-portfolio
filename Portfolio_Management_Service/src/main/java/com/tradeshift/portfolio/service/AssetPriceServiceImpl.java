package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.exception.AssetNotFoundException;
import com.tradeshift.portfolio.exception.PortfolioNotFoundException;
import com.tradeshift.portfolio.model.AssetPrice;
import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.model.Portfolio;
import com.tradeshift.portfolio.repository.AssetPriceRepository;
import com.tradeshift.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetPriceServiceImpl implements AssetPriceService {

    private final AssetPriceRepository assetPriceRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public AssetPrice getCurrentPrice(String assetSymbol) {
        // Step 1: Find the asset price from database
        AssetPrice assetPrice = assetPriceRepository.findByAssetSymbol(assetSymbol);
        
        // Step 2: Check if asset exists in our database
        if (assetPrice == null) {
            throw new AssetNotFoundException("Asset not found with symbol: " + assetSymbol);
        }
        
        // Step 3: Return the current price
        return assetPrice;
    }

    @Override
    public List<AssetPrice> getCurrentPrices(List<String> assetSymbols) {
        // Step 1: Check if input list is empty or null
        if (assetSymbols == null || assetSymbols.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Step 2: Use repository method to get multiple assets at once
        List<AssetPrice> assetPrices = assetPriceRepository.findByAssetSymbolIn(assetSymbols);
        
        // Step 3: Return the list of found asset prices
        return assetPrices != null ? assetPrices : new ArrayList<>();
    }

    @Override
    public AssetPrice updateAssetPrice(String assetSymbol, double newPrice) {
        // Step 1: Validate input parameters
        if (assetSymbol == null || assetSymbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset symbol cannot be null or empty");
        }
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        // Step 2: Find existing asset price record
        AssetPrice existingAsset = assetPriceRepository.findByAssetSymbol(assetSymbol);
        
        // Step 3: Check if asset exists in database
        if (existingAsset == null) {
            throw new AssetNotFoundException("Asset not found with symbol: " + assetSymbol);
        }
        
        // Step 4: Store previous price for change calculation
        double previousPrice = existingAsset.getCurrentPrice();
        
        // Step 5: Update the price fields
        existingAsset.setCurrentPrice(newPrice);
        existingAsset.setPreviousClosePrice(previousPrice);
        existingAsset.setChangeAmount(newPrice - previousPrice);
        existingAsset.setChangePercentage(previousPrice > 0 ? 
            ((newPrice - previousPrice) / previousPrice) * 100 : 0.0);
        existingAsset.setLastUpdated(LocalDateTime.now().toString());
        
        // Step 6: Save updated asset price to database
        AssetPrice updatedAsset = assetPriceRepository.save(existingAsset);
        
        // Step 7: Return the updated asset price
        return updatedAsset;
    }

    @Override
    public List<AssetPrice> updateAssetPrices(List<String> assetSymbols, List<Double> newPrices) {
        // Step 1: Validate input parameters
        if (assetSymbols == null || newPrices == null) {
            throw new IllegalArgumentException("Asset symbols and prices cannot be null");
        }
        if (assetSymbols.size() != newPrices.size()) {
            throw new IllegalArgumentException("Asset symbols and prices lists must have same size");
        }
        if (assetSymbols.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Step 2: Validate all prices are positive
        for (Double price : newPrices) {
            if (price <= 0) {
                throw new IllegalArgumentException("All prices must be greater than 0");
            }
        }
        
        // Step 3: Get existing assets from database
        List<AssetPrice> existingAssets = assetPriceRepository.findByAssetSymbolIn(assetSymbols);
        
        // Step 4: Update each asset price
        List<AssetPrice> updatedAssets = new ArrayList<>();
        for (int i = 0; i < assetSymbols.size(); i++) {
            String assetSymbol = assetSymbols.get(i);
            double newPrice = newPrices.get(i);
            
            // Find existing asset
            AssetPrice existingAsset = existingAssets.stream()
                    .filter(asset -> asset.getAssetSymbol().equals(assetSymbol))
                    .findFirst()
                    .orElse(null);
            
            if (existingAsset != null) {
                // Store previous price
                double previousPrice = existingAsset.getCurrentPrice();
                
                // Update price fields
                existingAsset.setCurrentPrice(newPrice);
                existingAsset.setPreviousClosePrice(previousPrice);
                existingAsset.setChangeAmount(newPrice - previousPrice);
                existingAsset.setChangePercentage(previousPrice > 0 ? 
                    ((newPrice - previousPrice) / previousPrice) * 100 : 0.0);
                existingAsset.setLastUpdated(LocalDateTime.now().toString());
                
                updatedAssets.add(existingAsset);
            }
        }
        
        // Step 5: Save all updated assets
        List<AssetPrice> savedAssets = assetPriceRepository.saveAll(updatedAssets);
        
        // Step 6: Return updated assets
        return savedAssets;
    }

    @Override
    public List<AssetPrice> getAssetsByType(String assetType) {
        // Step 1: Validate input parameter
        if (assetType == null || assetType.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset type cannot be null or empty");
        }
        
        // Step 2: Get all assets of specified type from database
        List<AssetPrice> assets = assetPriceRepository.findByAssetType(assetType);
        
        // Step 3: Return the list of assets
        return assets != null ? assets : new ArrayList<>();
    }

    @Override
    public void refreshPortfolioPrices(String portfolioId) {
        // Step 1: Find the portfolio by ID
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with ID: " + portfolioId));
        
        // Step 2: Get all holdings from the portfolio
        List<Holding> holdings = portfolio.getHoldings();
        
        // Step 3: Check if portfolio has any holdings
        if (holdings == null || holdings.isEmpty()) {
            return; // Nothing to refresh
        }
        
        // Step 4: Extract all asset symbols from holdings
        List<String> assetSymbols = new ArrayList<>();
        for (Holding holding : holdings) {
            assetSymbols.add(holding.getAssetSymbol());
        }
        
        // Step 5: Get latest prices for all assets in one query
        List<AssetPrice> latestPrices = assetPriceRepository.findByAssetSymbolIn(assetSymbols);
        
        // Step 6: Update each holding with latest price
        for (Holding holding : holdings) {
            // Find the corresponding price for this holding
            AssetPrice currentPrice = latestPrices.stream()
                    .filter(price -> price.getAssetSymbol().equals(holding.getAssetSymbol()))
                    .findFirst()
                    .orElse(null);
            
            // Step 7: Update holding if price found
            if (currentPrice != null) {
                // Update current price
                holding.setCurrentPrice(currentPrice.getCurrentPrice());
                
                // Recalculate current value
                holding.setCurrentValue(holding.getQuantity() * holding.getCurrentPrice());
                
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
        }
        
        // Step 8: Save updated portfolio with refreshed holdings
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);
    }

}
