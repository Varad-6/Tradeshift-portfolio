package com.tradeshift.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Field("asset_symbol")
    private String assetSymbol; // e.g., "AAPL", "BTC", "INFY"

    @Field("asset_name")
    private String assetName; // e.g., "Apple Inc.", "Bitcoin"

    @Field("asset_type")
    private String assetType; // e.g., STOCK, CRYPTO, MUTUAL_FUND

    @Field("quantity")
    private double quantity; // how many units user owns

    @Field("avg_buy_price")
    private double avgBuyPrice; // average cost price per unit

    @Field("current_price")
    private double currentPrice; // will be updated from AssetPrice service

    @Field("current_value")
    private double currentValue; // quantity * currentPrice

    @Field("invested_value")
    private double investedValue; // quantity * avgBuyPrice

    @Field("profit_loss")
    private double profitLoss; // currentValue - investedValue

    @Field("change_percentage")
    private double changePercentage; // e.g. +1.45%

    @Field("last_updated")
    private LocalDateTime lastUpdated;
}
