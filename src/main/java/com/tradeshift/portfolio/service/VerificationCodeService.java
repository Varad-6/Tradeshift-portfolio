package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    void deleteVerificationCode(VerificationCode verificationCode);

    VerificationCode getVerificationCodeByUser(Long userId);

    VerificationCode saveVerificationCode(VerificationCode verificationCode);
}
