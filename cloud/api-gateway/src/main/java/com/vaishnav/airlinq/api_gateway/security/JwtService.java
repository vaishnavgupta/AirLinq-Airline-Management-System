package com.vaishnav.airlinq.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKeyString;

    public Claims validateToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(
                secretKeyString.getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
