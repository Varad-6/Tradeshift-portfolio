package com.tradeshift.portfolio.service;

public interface SmsService {
    void sendOtpSms(String mobileNumber, String otp);
}
