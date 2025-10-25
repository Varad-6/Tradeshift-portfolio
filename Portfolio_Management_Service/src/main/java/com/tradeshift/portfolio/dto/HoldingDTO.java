package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingDTO {

    private String assetSymbol;
    private String assetName;
    private String assetType;
    private double quantity;
    private double avgBuyPrice;
    private double currentPrice;
    private double currentValue;
    private double investedValue;
    private double profitLoss;
    private double changePercentage;
}

