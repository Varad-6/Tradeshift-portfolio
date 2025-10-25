package com.tradeshift.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                // Portfolio Management Service Routes

                // Asset Prices Controller
                .route("asset-prices", r -> r
                        .path("/asset-prices/**")
                        .uri("lb://PORTFOLIO-MANAGEMENT-SERVICE"))

                // Holdings Controller
                .route("holdings", r -> r
                        .path("/holdings/**")
                        .uri("lb://PORTFOLIO-MANAGEMENT-SERVICE"))

                // Analytics Controller
                .route("analytics", r -> r
                        .path("/analytics/**")
                        .uri("lb://PORTFOLIO-MANAGEMENT-SERVICE"))

                // Portfolios Controller
                .route("portfolios", r -> r
                        .path("/portfolios/**")
                        .uri("lb://PORTFOLIO-MANAGEMENT-SERVICE"))

                // Transactions Controller
                .route("transactions", r -> r
                        .path("/transactions/**")
                        .uri("lb://PORTFOLIO-MANAGEMENT-SERVICE"))

                .build();
    }
}
