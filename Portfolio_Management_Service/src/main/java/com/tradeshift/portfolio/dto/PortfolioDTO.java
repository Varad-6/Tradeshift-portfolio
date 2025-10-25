package com.tradeshift.portfolio.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
    private String userId;
    private String portfolioName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<HoldingDTO> holdings;

}
