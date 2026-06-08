package com.vaishnav.airlinq.user.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String secretKeyString;

    @Value("${jwt.expiration-ms}")
    private long expirationTime;


    public String generateJwtToken(Authentication auth, Long userId) {
        SecretKey secretKey = Keys.hmacShaKeyFor(
                secretKeyString.getBytes()
        );
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateRoles(authorities);
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime) )
                .claim("email", auth.getName())
                .claim("userId", userId)
                .claim("authorities", roles)
                .signWith(secretKey)
                .compact();
    }

    private String populateRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority grantedAuthority : authorities) {
            auth.add(grantedAuthority.getAuthority());
        }
        return String.join(",", auth);
    }

}
