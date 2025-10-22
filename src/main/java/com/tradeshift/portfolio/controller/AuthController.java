package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.model.TwoFactorOTP;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.otp.OtpUtils;
import com.tradeshift.portfolio.repository.UserRepository;
import com.tradeshift.portfolio.response.AuthResponse;
import com.tradeshift.portfolio.security.JwtProvider;
import com.tradeshift.portfolio.service.CustomUserDetailsService;
import com.tradeshift.portfolio.service.EmailService;
import com.tradeshift.portfolio.service.TwoFactorOTPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private TwoFactorOTPService twoFactorOTPService;
    private EmailService emailService;

    // inject both through constructor
    public AuthController(UserRepository userRepository, EmailService emailService,TwoFactorOTPService twoFactorOTPService,PasswordEncoder passwordEncoder,CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.twoFactorOTPService=twoFactorOTPService;
        this.emailService=emailService;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService=customUserDetailsService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{

        // Check if email already exists
        User isEmailExist=userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email already in use with another account");
        }

        // Create new user
        User newUser=new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setMobile(user.getMobile());
        newUser.setRole(user.getRole());
        
        userRepository.save(newUser);

        // Auto-login after successful registration with authorities
       UserDetails userDetails = customUserDetailsService.loadUserByUsername(newUser.getEmail());
       Authentication authentication = new UsernamePasswordAuthenticationToken(
		          userDetails,
	    	null,
		        userDetails.getAuthorities()
        ); 

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT with authorities
        String jwtToken = JwtProvider.generateToken(authentication);

        // Return success response with JWT
        AuthResponse authResonse=new AuthResponse();
        authResonse.setJwt(jwtToken);
        authResonse.setStatus(true);
        authResonse.setMessage("User registered successfully");
        return new ResponseEntity<>(authResonse,HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{

        String userName= user.getEmail();
        String passWord=user.getPassword();

        // Auto-login after successful registration
        Authentication authentication=authenticate(userName,passWord);

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Save user to the database
        String jwtToken= JwtProvider.generateToken(authentication);

        User authUser= userRepository.findByEmail(userName);

        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res=new AuthResponse();
            res.setMessage("Two Factor Auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp= OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOtp=twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOTPService.deleteOTP(oldTwoFactorOtp);
            }

            TwoFactorOTP newTwoFactorOtp=twoFactorOTPService.createOTP(authUser,otp,jwtToken);

            // Send OTP to user's email
            emailService.sendVerificationOtpEmail(userName,otp);

            res.setSession(newTwoFactorOtp.getId());

            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);

        }

        AuthResponse authResonse=new AuthResponse();
        authResonse.setJwt(jwtToken);
        authResonse.setStatus(true);
        authResonse.setMessage("login success");
        return new ResponseEntity<>(authResonse,HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String passWord) {

        UserDetails userDetails=customUserDetailsService.loadUserByUsername(userName);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid UserName");
        }
        if (!passwordEncoder.matches(passWord, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,passWord,userDetails.getAuthorities());
    }

    @PostMapping("/verify-signin-otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp, @RequestParam String id){
        TwoFactorOTP twoFactorOTP=twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyOTP(twoFactorOTP,otp)){
            AuthResponse authResonse=new AuthResponse();
            authResonse.setMessage("OTP verified successfully");
            authResonse.setTwoFactorAuthEnabled(true);
            authResonse.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(authResonse,HttpStatus.OK);
        }
        throw new BadCredentialsException("Invalid OTP");
    }
}

