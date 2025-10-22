package com.tradeshift.portfolio.repository;

import com.tradeshift.portfolio.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,String> {
   // ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUserId(Long userId);
}
