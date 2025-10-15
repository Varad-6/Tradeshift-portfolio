package com.tradeshift.portfolio.service;

import com.tradeshift.portfolio.model.User;
import com.tradeshift.portfolio.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // This method is used by Spring Security to load user-specific data.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found with email: "+username);
        }
        // For simplicity, we are not assigning any roles or authorities here.
        List<GrantedAuthority> authorities= new ArrayList<>();

        // You can add roles or authorities to the list if needed.
        return new org.springframework.security.core.
                userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities
        );

    }
}
