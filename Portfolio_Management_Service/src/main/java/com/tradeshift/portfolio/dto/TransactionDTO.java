package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String userId;
    private String assetSymbol;
    private String assetName;
    private String assetType;
    private String transactionType;
    private double quantity;
    private double pricePerUnit;
    private double totalValue;
    private double fees;
    private LocalDateTime transactionDate;
    private String remarks;
}
