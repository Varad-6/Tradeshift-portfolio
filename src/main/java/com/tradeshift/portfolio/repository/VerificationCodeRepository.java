package com.tradeshift.portfolio.repository;

import com.tradeshift.portfolio.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    public VerificationCode findByUserId(Long userId);

}
