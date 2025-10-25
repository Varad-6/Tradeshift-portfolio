package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetPerformanceDTO {

    private String assetSymbol;
    private String assetName;
    private String assetType;
    private double currentValue;
    private double investedValue;
    private double profitLoss;
    private double changePercentage;
    private double portfolioAllocation; // Percentage of total portfolio
    private int rank; // Performance rank (1 = best performer)
}
