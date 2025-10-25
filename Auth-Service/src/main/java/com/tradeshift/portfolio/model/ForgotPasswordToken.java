package com.tradeshift.portfolio.model;

import com.tradeshift.portfolio.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private User user;

    private String otp;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    private String sendTo;
}
