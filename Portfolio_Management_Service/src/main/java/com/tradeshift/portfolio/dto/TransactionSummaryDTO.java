package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDTO {
    private int totalTransactions;
    private int buyTransactions;
    private int sellTransactions;
    private double totalBuyValue;
    private double totalSellValue;
    private double netTransactionValue;
    private double totalFees;
    private String mostTradedAsset;
    private int mostTradedAssetCount;
}