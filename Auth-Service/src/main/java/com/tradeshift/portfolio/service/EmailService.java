package com.tradeshift.portfolio.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationOtpEmail(String email,String otp) throws MessagingException {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,"utf-8");
        String subject="TradeShift - Email Verification OTP";
        String text="Your TradeShift verification code is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\nIf you didn't request this, please ignore this email.";

        mimeMessageHelper.setFrom("padianilanil143@gmail.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);

        try{
            javaMailSender.send(mimeMessage);
            System.out.println("Email sent successfully to: " + email);
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new MailSendException(e.getMessage());
        }

    }

}
