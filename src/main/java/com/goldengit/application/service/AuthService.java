package com.goldengit.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    @Value("${goldengit.jwt.key}")
    private String jwtKey;


    @Cacheable("jwt-tokens")
    public String generateToken(String visitorId) throws BadRequestException {
        validateVisitorId(visitorId);
        return createToken(visitorId);
    }

    private String createToken(String visitorId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims(claims).subject(visitorId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void validateVisitorId(String visitorId) throws BadRequestException {
        if (!(visitorId != null && visitorId.matches("[0-9a-fA-F]{32}"))) {
            throw new BadRequestException("Invalid visitor ID");
        }

    }
}
