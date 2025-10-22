package com.tradeshift.portfolio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component // 👈 Add this so Spring manages this bean
public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_Header);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> authoritiesList =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authoritiesList
                );

                // ✅ Set authentication context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("✅ JWT validated for user: " + email);

            } catch (Exception e) {
                System.out.println("❌ Invalid JWT: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No JWT found in header");
        }

        filterChain.doFilter(request, response);
    }
}


/*
package com.tradeshift.portfolio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenValidator extends OncePerRequestFilter{

    // This method will validate the JWT token from the request header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt=request.getHeader(JwtConstant.JWT_Header);

        // Remove "Bearer " prefix if present
        if(jwt!=null){
            jwt=jwt.substring(7);

            try{
                // Parse the token
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                // Extract claims from the token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Extract email and authorities from claims
                String email=String.valueOf(claims.get("email"));

                String authorities=String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> authoritiesList= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create an Authentication object
                Authentication authentication=new UsernamePasswordAuthenticationToken
                        (
                                email,
                                null,
                                authoritiesList

                        );

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw new RuntimeException("Invalid Token");
            }
        }
        // Continue the filter chain
        filterChain.doFilter(request,response);
    }

}
*/
