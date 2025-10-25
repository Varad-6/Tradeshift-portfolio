package com.tradeshift.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "asset_prices")
public class AssetPrice {

    @Id
    private String id;

    @Field("asset_symbol")
    private String assetSymbol; // e.g. AAPL, BTC, GOOGL

    @Field("asset_name")
    private String assetName; // e.g. Apple Inc., Bitcoin

    @Field("asset_type")
    private String assetType; // STOCK, CRYPTO, MUTUAL_FUND

    @Field("current_price")
    private double currentPrice; // Latest market price

    @Field("previous_close_price")
    private double previousClosePrice; // For daily change calculations

    @Field("change_amount")
    private double changeAmount; // current - previous

    @Field("change_percentage")
    private double changePercentage; // (changeAmount / previousClosePrice) * 100

    @Field("last_updated")
    private String lastUpdated; //  Timestamp when data was refreshed

}
