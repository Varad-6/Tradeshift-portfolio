package com.tradeshift.portfolio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {

    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // Method to generate JWT token
    public static String generateToken(Authentication authentication) {
        // Extract authorities and convert to comma-separated string
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles=populateAuthorities(authorities);

        // Generate JWT token with claims
        String jwt= Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JwtConstant.EXPIRATION_TIME))
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(SIGNING_KEY)
                .compact();
        return jwt;
    }

    // Method to extract email from the JWT token
    public static String getEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email=String.valueOf(claims.get("email"));
        return email;
    }

    // Helper method to convert authorities to a comma-separated string
    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth=new HashSet<>();

        for(GrantedAuthority authority:authorities){
            auth.add(authority.getAuthority());
        }
        // Join the set into a single comma-separated string
        return String.join(",",auth);
    }
}
