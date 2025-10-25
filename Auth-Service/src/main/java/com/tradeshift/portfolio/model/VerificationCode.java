package com.tradeshift.portfolio.model;

import com.tradeshift.portfolio.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "verificationType"})
})
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String email;

    private String mobile;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;
}
