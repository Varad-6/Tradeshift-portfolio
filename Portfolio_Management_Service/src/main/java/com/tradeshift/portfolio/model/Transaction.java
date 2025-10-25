package com.tradeshift.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    @Field("user_id")
    private String userId; // Owner of the transaction

    @Field("asset_symbol")
    private String assetSymbol; // e.g., AAPL, BTC

    @Field("asset_name")
    private String assetName; // e.g., Apple Inc., Bitcoin

    @Field("asset_type")
    private String assetType; // STOCK, CRYPTO, MUTUAL_FUND

    @Field("transaction_type")
    private String transactionType; // BUY or SELL

    @Field("quantity")
    private double quantity; // Number of units bought or sold

    @Field("price_per_unit")
    private double pricePerUnit; // Price at which transaction executed

    @Field("total_value")
    private double totalValue; // quantity * pricePerUnit

    @Field("fees")
    private double fees; // Transaction fees if any

    @Field("transaction_date")
    private LocalDateTime transactionDate; // When transaction happened

    @Field("remarks")
    private String remarks; // Optional notes for the transaction

}
