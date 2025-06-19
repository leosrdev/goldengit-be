package com.goldengit.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService {
    @Value("${goldengit.jwt.key}")
    private String jwtKey;


    @Cacheable("jwt-tokens")
    public String generateToken(String visitorId) throws BadRequestException {
        validateVisitorId(visitorId);
        return createToken(visitorId);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims(claims).subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plusSeconds(3600*24*7)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void validateVisitorId(String visitorId) throws BadRequestException {
        if (!(visitorId != null && visitorId.matches("[0-9a-fA-F]{32}"))) {
            throw new BadRequestException("Invalid visitor ID");
        }

    }
}
