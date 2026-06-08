package com.vaishnav.airlinq.api_gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {

    private final JwtService jwtService;

    public HandlerFilterFunction<ServerResponse, ServerResponse> requireAuth() {
        return (request, next) -> {
            String token = extractToken(request);

            if(token == null) {
                return ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Missing Authorization header");
            }

            try {
                Claims claims = jwtService.validateToken(token);

                ServerRequest modifiedRequest = ServerRequest.from(request)
                        .header("X-User-Id", String.valueOf(claims.get("userId")))
                        .header("X-User-Email", String.valueOf(claims.get("email")))
                        .header("X-User-Role", String.valueOf(claims.get("authorities")))
                        .build();

                return next.handle(modifiedRequest);
            } catch (Exception e) {
                return ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired token");
            }
        };
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> requireRole(String... allowedRoles) {
        return (request, next) -> {
            String token = extractToken(request);

            if (token == null) {
                return ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Missing Authorization header");
            }

            try {
                Claims claims = jwtService.validateToken(token);
                String authorities = String.valueOf(claims.get("authorities"));

                boolean allowed = Arrays.stream(allowedRoles)
                        .anyMatch(authorities::contains);

                if (!allowed) {
                    return ServerResponse
                            .status(HttpStatus.FORBIDDEN)
                            .body("Access denied");
                }

                ServerRequest modifiedRequest = ServerRequest.from(request)
                        .header("X-User-Id", String.valueOf(claims.get("userId")))
                        .header("X-User-Email", String.valueOf(claims.get("email")))
                        .header("X-User-Role", authorities)
                        .build();

                return next.handle(modifiedRequest);
            } catch (Exception e) {
                return ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired token");
            }
        };
    }

    private String extractToken(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }

}
