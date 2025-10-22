package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.model.TwoFactorOTP;
import com.tradeshift.portfolio.model.User;


public interface TwoFactorOTPService {

    TwoFactorOTP createOTP(User user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyOTP(TwoFactorOTP twoFactorOTP, String otp);

    void deleteOTP(TwoFactorOTP twoFactorOTP);


}
