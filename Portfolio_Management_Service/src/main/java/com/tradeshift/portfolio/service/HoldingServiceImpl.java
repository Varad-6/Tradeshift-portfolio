package com.tradeshift.portfolio.service;

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
public class HoldingServiceImpl implements HoldingService {

    private final PortfolioRepository portfolioRepository;

    @Override
    public Holding addHolding(String portfolioId, Holding holding) {
        //check portfolio exist or not
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Portfolio not found with ID: "
                                + portfolioId));

        //create a list for storing holdings
        List<Holding> holdings = portfolio.getHoldings();

        if (holdings == null) {
            holdings = new ArrayList<>();
        }
        //check if asset symbol is already exist if not add holding and
        boolean exists = holdings.stream()
                .anyMatch(h -> h.getAssetSymbol().equals(holding.getAssetSymbol()));

        if (exists) {
            throw new IllegalArgumentException("Holding already exists for asset: " + holding.getAssetSymbol());
        }

        holding.setLastUpdated(LocalDateTime.now());
        holdings.add(holding);
        portfolio.setHoldings(holdings);
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);

        return holding;
    }

    @Override
    public Holding updateHolding(String portfolioId, String assetSymbol, Holding updatedHolding) {

        //check portfolio exist or not
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Portfolio not found with ID: "
                                + portfolioId));
        //storing holdings into a list and checking holdings are null or empty
        List<Holding> holdings = portfolio.getHoldings();
        if (holdings == null || holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings available to update");
        }
        //checking asset is already exist in list or not
        Optional<Holding> existing = holdings.stream()
                .filter(h -> h.getAssetSymbol().equals(assetSymbol))
                .findFirst();
        //if not exist in list throw error
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Holding not found for asset: " + assetSymbol);
        }
        //if exist update with new asset values
        Holding holding = existing.get();
        holding.setQuantity(updatedHolding.getQuantity());
        holding.setAvgBuyPrice(updatedHolding.getAvgBuyPrice());
        holding.setCurrentPrice(updatedHolding.getCurrentPrice());
        holding.setCurrentValue(updatedHolding.getCurrentValue());
        holding.setInvestedValue(updatedHolding.getInvestedValue());
        holding.setProfitLoss(updatedHolding.getProfitLoss());
        holding.setChangePercentage(updatedHolding.getChangePercentage());
        holding.setLastUpdated(LocalDateTime.now());

        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);

        return holding;
    }

    @Override
    public void removeHolding(String portfolioId, String assetSymbol) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));

        List<Holding> holdings = portfolio.getHoldings();
        if (holdings == null) return;

        holdings.removeIf(h -> h.getAssetSymbol().equals(assetSymbol));
        portfolio.setHoldings(holdings);
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);
    }

    @Override
    public List<Holding> getHoldingsByPortfolio(String portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));

        return portfolio.getHoldings() != null ? portfolio.getHoldings() : new ArrayList<>();
    }

    @Override
    public Holding getHoldingByAsset(String portfolioId, String assetSymbol) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));

        return portfolio.getHoldings().stream()
                .filter(h -> h.getAssetSymbol().equals(assetSymbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Holding not found for asset: " + assetSymbol));
    }

    @Override
    public void updateHoldingPrices(String portfolioId, String assetSymbol, double currentPrice) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));

        Holding holding = portfolio.getHoldings().stream()
                .filter(h -> h.getAssetSymbol().equals(assetSymbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Holding not found for asset: " + assetSymbol));

        holding.setCurrentPrice(currentPrice);
        holding.setCurrentValue(holding.getQuantity() * currentPrice);
        holding.setProfitLoss(holding.getCurrentValue() - holding.getInvestedValue());
        holding.setChangePercentage(
                holding.getInvestedValue() > 0 ?
                        (holding.getProfitLoss() / holding.getInvestedValue()) * 100 : 0.0
        );
        holding.setLastUpdated(LocalDateTime.now());

        portfolio.setUpdatedAt(LocalDateTime.now());

        portfolioRepository.save(portfolio);
    }
}
