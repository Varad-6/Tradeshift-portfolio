package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetPriceDTO {

    private String assetSymbol;       // e.g., AAPL, BTC
    private String assetName;         // e.g., Apple Inc., Bitcoin
    private String assetType;         // STOCK, CRYPTO, MUTUAL_FUND
    private double currentPrice;      // Latest market price
    private double previousClosePrice; // For daily change calculation
    private double changeAmount;      // currentPrice - previousClosePrice
    private double changePercentage;  // (changeAmount / previousClosePrice) * 100
    private String lastUpdated;       // Timestamp of last price update
}

