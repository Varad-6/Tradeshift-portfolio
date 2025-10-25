package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.model.AssetPrice;
import com.tradeshift.portfolio.security.JwtUtil;
import com.tradeshift.portfolio.service.AssetPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset-prices")
@RequiredArgsConstructor
public class AssetPriceController {

    private final AssetPriceService assetPriceService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{assetSymbol}")
    public ResponseEntity<AssetPrice> getCurrentPrice(@PathVariable String assetSymbol) {
        try {
            AssetPrice assetPrice = assetPriceService.getCurrentPrice(assetSymbol);
            return new ResponseEntity<>(assetPrice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<List<AssetPrice>> getCurrentPrices(@RequestBody List<String> assetSymbols) {
        try {
            List<AssetPrice> assetPrices = assetPriceService.getCurrentPrices(assetSymbols);
            return new ResponseEntity<>(assetPrices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{assetSymbol}")
    public ResponseEntity<AssetPrice> updateAssetPrice(@RequestHeader("Authorization") String jwt,
                                                       @PathVariable String assetSymbol,
                                                       @RequestParam double newPrice) {
        try {
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Check if user has ADMIN role
            if (!jwtUtil.hasRole(jwt, "ADMIN")) {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
            
            AssetPrice updatedAsset = assetPriceService.updateAssetPrice(assetSymbol, newPrice);
            return new ResponseEntity<>(updatedAsset, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/batch")
    public ResponseEntity<List<AssetPrice>> updateAssetPrices(@RequestHeader("Authorization") String jwt,
                                                              @RequestBody List<String> assetSymbols,
                                                              @RequestBody List<Double> newPrices) {
        try {
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            // Check if user has ADMIN role
            if (!jwtUtil.hasRole(jwt, "ADMIN")) {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
            
            List<AssetPrice> updatedAssets = assetPriceService.updateAssetPrices(assetSymbols, newPrices);
            return new ResponseEntity<>(updatedAssets, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/type/{assetType}")
    public ResponseEntity<List<AssetPrice>> getAssetsByType(@PathVariable String assetType) {
        try {
            List<AssetPrice> assets = assetPriceService.getAssetsByType(assetType);
            return new ResponseEntity<>(assets, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/portfolio/{portfolioId}/refresh")
    public ResponseEntity<String> refreshPortfolioPrices(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable String portfolioId) {
        try {
            // Validate JWT token
            if (!jwtUtil.validateToken(jwt)) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            
            // Get user email from JWT token
            String userEmail = jwtUtil.getEmailFromToken(jwt);
            if (userEmail == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            
            assetPriceService.refreshPortfolioPrices(portfolioId);
            return new ResponseEntity<>("Portfolio prices refreshed successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Portfolio not found with ID: " + portfolioId, HttpStatus.NOT_FOUND);
        }
    }

    // Helper endpoint to create sample data for testing
    @PostMapping("/create-sample-data")
    public ResponseEntity<String> createSampleData() {
        try {
            // This is just for testing - in real app, data comes from market feeds

            return new ResponseEntity<>("Sample data creation endpoint - implement as needed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating sample data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
