package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.model.TwoFactorOTP;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.repository.TwoFactorOtpRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService{

    private TwoFactorOtpRepository twoFactorOtpRepository;

    public TwoFactorOTPServiceImpl(TwoFactorOtpRepository twoFactorOtpRepository) {
        this.twoFactorOtpRepository = twoFactorOtpRepository;
    }

    @Override
    public TwoFactorOTP createOTP(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();
        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setId(id);
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setJwt(jwt);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> OTP = twoFactorOtpRepository.findById(id);
        return OTP.orElse(null);
    }

    @Override
    public boolean verifyOTP(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteOTP(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepository.delete(twoFactorOTP);
    }
}
