package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.model.Holding;
import com.tradeshift.portfolio.service.HoldingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holdings")
@RequiredArgsConstructor
public class HoldingController {

    private final HoldingService holdingService;

    @PostMapping("/{portfolioId}")
    public ResponseEntity<Holding> addHolding(
            @PathVariable String portfolioId,
            @RequestBody Holding holding){

            Holding created = holdingService.addHolding(portfolioId, holding);
            return ResponseEntity.ok(created);
    }

    @PutMapping("/{portfolioId}/{assetSymbol}")
    public ResponseEntity<Holding> updateHolding(
            @PathVariable String portfolioId,
            @PathVariable String assetSymbol,
            @RequestBody Holding updatedHolding) {
        Holding updated = holdingService.updateHolding(portfolioId, assetSymbol, updatedHolding);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{portfolioId}/{assetSymbol}")
    public ResponseEntity<Void> removeHolding(
            @PathVariable String portfolioId,
            @PathVariable String assetSymbol) {
        holdingService.removeHolding(portfolioId, assetSymbol);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<List<Holding>> getHoldingsByPortfolio(
            @PathVariable String portfolioId) {
        List<Holding> holdings = holdingService.getHoldingsByPortfolio(portfolioId);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/{portfolioId}/{assetSymbol}")
    public ResponseEntity<Holding> getHoldingByAsset(
            @PathVariable String portfolioId,
            @PathVariable String assetSymbol) {
        Holding holding = holdingService.getHoldingByAsset(portfolioId, assetSymbol);
        return ResponseEntity.ok(holding);
    }

    @PatchMapping("/{portfolioId}/{assetSymbol}/price")
    public ResponseEntity<Void> updateHoldingPrice(
            @PathVariable String portfolioId,
            @PathVariable String assetSymbol,
            @RequestParam double currentPrice) {
        holdingService.updateHoldingPrices(portfolioId, assetSymbol, currentPrice);
        return ResponseEntity.noContent().build();
    }
}
