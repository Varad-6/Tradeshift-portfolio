
package com.tradeshift.portfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${sms.provider}")
    private String smsProvider;

    @Value("${sms.fast2sms.api-key}")
    private String fast2smsApiKey;

    @Value("${sms.fast2sms.sender-id}")
    private String senderId;

    private final RestTemplate restTemplate;

    @Autowired
    public SmsServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendOtpSms(String mobileNumber, String otp) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid mobile number: " + mobileNumber);
        }

        if ("fast2sms".equalsIgnoreCase(smsProvider)) {
            sendViaFast2Sms(mobileNumber, otp);
        } else {
            log.info("SMS OTP for {}: {}", mobileNumber, otp);
        }
    }

    private void sendViaFast2Sms(String mobileNumber, String otp) {
        try {
            String url = "https://www.fast2sms.com/dev/bulkV2";
            String message = "Your TradeShift OTP is: " + otp + ". Valid for 5 minutes.";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("authorization", fast2smsApiKey);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("sender_id", senderId);
            body.add("message", message);
            body.add("language", "english");
            body.add("route", "v3");
            body.add("numbers", mobileNumber);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Fast2SMS response for {}: {}", mobileNumber, response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("SMS sending failed for {}: HTTP {}", mobileNumber, response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Failed to send SMS via Fast2SMS for {}: {}", mobileNumber, e.getMessage(), e);
            log.info("Fallback SMS OTP for {}: {}", mobileNumber, otp);
        }
    }
}


