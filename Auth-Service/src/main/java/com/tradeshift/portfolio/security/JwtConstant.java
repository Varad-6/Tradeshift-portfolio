package com.tradeshift.portfolio.security;

public class JwtConstant {
    public static final String SECRET_KEY= "ThisIsAVeryLongSecretKeyForJWTSigning1234567890!";
    public static final long EXPIRATION_TIME=86400000; // 1 day in milliseconds
    public static final String JWT_Header="Authorization";
}
