package com.tradeshift.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioSummaryDTO {

    private double totalInvestedValue;
    private double currentMarketValue;
    private double totalProfitLoss;
    private double totalChangePercentage;
}
