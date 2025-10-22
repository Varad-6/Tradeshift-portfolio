package com.tradeshift.portfolio.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class AuthResponse {

    private String jwt;
    private boolean status;
    private String message;
    private boolean isTwoFactorAuthEnabled;
    private String session;

}
