package com.tradeshift.portfolio.exception;

public class PortfolioNotFoundException extends RuntimeException {
    
    public PortfolioNotFoundException(String message) {
        super(message);
    }
    
    public PortfolioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
