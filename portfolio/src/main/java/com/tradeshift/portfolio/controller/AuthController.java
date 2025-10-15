package com.tradeshift.portfolio.controller;

import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.repository.UserRepository;
import com.tradeshift.portfolio.response.AuthResonse;
import com.tradeshift.portfolio.security.JwtProvider;
import com.tradeshift.portfolio.service.CustomUserDetailsService;
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

    // inject both through constructor
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService=customUserDetailsService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResonse> register(@RequestBody User user) throws Exception{

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
        newUser.setRole(user.getRole());
        
        userRepository.save(newUser);

        // Auto-login after successful registration
        Authentication authentication=new UsernamePasswordAuthenticationToken
                (
                        user.getEmail(),
                        user.getPassword()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Save user to the database
        String jwtToken= JwtProvider.generateToken(authentication);

        AuthResonse authResonse=new AuthResonse();
        authResonse.setJwt(jwtToken);
        authResonse.setStatus(true);
        authResonse.setMessage("User registered successfully");
        return new ResponseEntity<>(authResonse,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResonse> login(@RequestBody User user) throws Exception{

        String userName= user.getEmail();
        String passWord=user.getPassword();

        // Auto-login after successful registration
        Authentication authentication=authenticate(userName,passWord);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Save user to the database
        String jwtToken= JwtProvider.generateToken(authentication);

        AuthResonse authResonse=new AuthResonse();
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
}

