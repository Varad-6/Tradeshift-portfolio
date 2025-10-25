package com.tradeshift.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "portfolios")
public class Portfolio {

    @Id
    private String id;

    @Field("user_id")
    private String userId; // eg: "user123", "investor_456"

    @Field("portfolio_name")
    private String portfolioName;// e.g: "Retirement Fund", "Crypto Holdings"

    @Field("description")
    private String description;// e.g: "Long-term investment portfolio", "High-risk crypto assets"

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("holdings")
    private List<Holding> holdings;

}
