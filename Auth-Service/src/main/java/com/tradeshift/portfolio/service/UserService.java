package com.tradeshift.portfolio.service;
import com.tradeshift.portfolio.domain.VerificationType;
import com.tradeshift.portfolio.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserProfileByEmail(String email) throws Exception;
    public User findUserProfileByMobile(String mobile) throws Exception;
    public User findUserProfileById(Long id) throws Exception;
    public User enabledTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user);
    User updatePassword(User user, String newPassword);
    void deleteUser(Long id);


}
