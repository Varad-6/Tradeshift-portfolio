package com.tradeshift.portfolio.security;

import com.tradeshift.portfolio.domain.VerificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    @NotBlank
    @Email // if sending via email
    private String sendTo;

    @NotNull
    private VerificationType verificationType;

}
