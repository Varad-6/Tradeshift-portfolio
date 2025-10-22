package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.ForgotPasswordToken;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.repository.ForgotPasswordRepository;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordImpl implements ForgotPasswordService{

    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordImpl(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }
    @Override
    public ForgotPasswordToken createToken(User user,
                                           String otp,
                                           VerificationType verificationType,
                                           String sendTo) {
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setOtp(otp);
        forgotPasswordToken.setVerificationType(verificationType);
        forgotPasswordToken.setSendTo(sendTo);

        return forgotPasswordRepository.save(forgotPasswordToken);
    }


    @Override
    public ForgotPasswordToken findById(String id) {
        return forgotPasswordRepository.findById(id).orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);

    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}
