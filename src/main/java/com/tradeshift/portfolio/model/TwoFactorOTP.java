package com.tradeshift.portfolio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TwoFactorOTP {
    @Id
    private String id;
    private String otp;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;

}
