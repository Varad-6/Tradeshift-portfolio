package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.ForgotPasswordToken;
import com.tradeshift.portfolio.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user,String otp,
                                    VerificationType verificationType,String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUserId(Long userId);

    void deleteToken(ForgotPasswordToken token);

}
