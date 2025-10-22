package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.ForgotPasswordToken;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.model.VerificationCode;
import com.tradeshift.portfolio.otp.OtpUtils;
import com.tradeshift.portfolio.response.ApiResponse;
import com.tradeshift.portfolio.response.AuthResponse;
import com.tradeshift.portfolio.security.ForgotPasswordTokenRequest;
import com.tradeshift.portfolio.security.ResetPasswordRequest;
import com.tradeshift.portfolio.service.EmailService;
import com.tradeshift.portfolio.service.ForgotPasswordService;
import com.tradeshift.portfolio.service.SmsService;
import com.tradeshift.portfolio.service.UserService;
import com.tradeshift.portfolio.service.VerificationCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    private UserService userService;
    private EmailService emailService;
    private SmsService smsService;
    private VerificationCodeService verificationCodeService;
    private ForgotPasswordService forgotPasswordService;

    public UserController(UserService userService, ForgotPasswordService forgotPasswordService, EmailService emailService, SmsService smsService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.forgotPasswordService = forgotPasswordService;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("verificationType") String verificationTypeParam
    ) throws Exception {

        VerificationType verificationType = VerificationType.valueOf(verificationTypeParam.toUpperCase());
        // Retrieve user profile from JWT
        User user = userService.findUserProfileByJwt(jwt);

        // Check if a verification code already exists for the user
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        } else {
            // Always generate a fresh OTP on resend and persist
            verificationCode.setVerificationType(verificationType);
            verificationCode.setOtp(OtpUtils.generateOtp());
        }

        // Record the destination contact for later verification step
        if (verificationType.equals(VerificationType.EMAIL)) {
            verificationCode.setEmail(user.getEmail());
        } else if (verificationType.equals(VerificationType.MOBILE)) {
            String mobileNumber = user.getMobile() != null ? user.getMobile() : "9100784590";
            verificationCode.setMobile(mobileNumber);
        }

        // Persist any changes to verificationCode
        verificationCode = verificationCodeService.saveVerificationCode(verificationCode);

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        } else if (verificationType.equals(VerificationType.MOBILE)) {
            // Send SMS to user's mobile number
            smsService.sendOtpSms(verificationCode.getMobile(), verificationCode.getOtp());
        }


        return new ResponseEntity<>(" Verification otp sent successfully ", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("users/enable-2fa/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp) throws Exception {
        // Retrieve user profile from JWT
        User user = userService.findUserProfileByJwt(jwt);
        // Retrieve the verification code associated with the user
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        // Determine the contact method used for verification
        String sendTo = verificationCode.getVerificationType() == VerificationType.EMAIL
                ? verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        // If OTP is verified, enable 2FA for the user
        if (isVerified) {
            User updatedUser = userService.enabledTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }

    // Forgot password - does not require authentication
    @PostMapping("users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordTokenRequest forgotPasswordTokenRequest
    ) throws Exception {
        // Retrieve user profile by email or mobile based on verification type
        User user;
        if (forgotPasswordTokenRequest.getVerificationType().equals(VerificationType.EMAIL)) {
            user = userService.findUserProfileByEmail(forgotPasswordTokenRequest.getSendTo());
        } else {
            user = userService.findUserProfileByMobile(forgotPasswordTokenRequest.getSendTo());
        }
        // Generate OTP and unique token ID
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        // Check if a forgot password token already exists for the user
        ForgotPasswordToken token = forgotPasswordService.findByUserId(user.getId());
        if (token == null) {
            token = forgotPasswordService.createToken(
                    user,
                    otp,
                    forgotPasswordTokenRequest.getVerificationType(),
                    forgotPasswordTokenRequest.getSendTo());
        }
        if (forgotPasswordTokenRequest.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @PatchMapping("users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> verifyresetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req) throws Exception {
        // Retrieve forgot password token by ID
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        // Verify the provided OTP
        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
        // If OTP is verified, update the user's password
        if (isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password update successfully");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("users/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        userService.deleteUser(user.getId());
        ApiResponse response = new ApiResponse();
        response.setMessage("User deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
