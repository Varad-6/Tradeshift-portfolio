package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.TwoFactorAuth;
import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.repository.UserRepository;
import com.tradeshift.portfolio.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User findUserProfileByEmail(String email) throws Exception {
            User user = userRepository.findByEmail(email);
            if (user == null){
                throw new Exception("User not found with email: " + email);
            }
        return user;
    }

    @Override
    public User findUserProfileByMobile(String mobile) throws Exception {
            User user = userRepository.findByMobile(mobile);
            if (user == null){
                throw new Exception("User not found with mobile: " + mobile);
            }
        return user;
    }

    @Override
    public User findUserProfileById(Long id) throws Exception {
        Optional<User> user= userRepository.findById(id);
        if(user.isEmpty()){
            throw new Exception("User not found with id: " + id);
        }
        return user.get();
    }
    @Override
    public User enabledTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user) {
        TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);


        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        //write logic if new password is same as old password throw a message cannot be same as old password
        if(user.getPassword().equals(newPassword)){
            throw new IllegalArgumentException("New password cannot be same as old password");
        }
        //user.setPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}