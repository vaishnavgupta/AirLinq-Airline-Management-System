package com.vaishnav.airlinq.api_gateway.config;

import com.vaishnav.airlinq.api_gateway.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouterFunction<ServerResponse> airlineServiceRoutes() {
        return route("airline-service")
                .route(
                        path("/api/airline/**").or(path("/api/aircraft/**")),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireRole(
                        "ROLE_SYSTEM_ADMIN",
                        "ROLE_AIRLINE_OWNER"
                ))
                .filter(lb("airline-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> publicUserServiceRoutes() {
        return route("public-user-service")
                .route(
                        path("/auth/login").or(path("/auth/signup")),
                        http()
                )
                .filter(lb("user-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> protectedUserServiceRoutes() {
        return route("protected-user-service")
                .route(
                        (path("/api/users/**")),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireAuth())
                .filter(lb("user-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceRoutes() {
        return route("location-service")
                .route(
                        path("/api/cities/**").or(path("/api/airport/**")),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireRole(
                        "ROLE_SYSTEM_ADMIN",
                        "ROLE_AIRLINE_OWNER"
                ))
                .filter(lb("location-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> flightOpsServiceRoutes() {
        return route("flight-ops-service")
                .route(
                        path("/api/flight/**")
                                .or(path("/api/flight-instance/**"))
                                .or(path("api/schedule/**")),
                        http()
                )
                .filter(lb("flight-ops-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pricingServiceRoutes() {
        return route("pricing-service")
                .route(
                        path("/api/baggage-policies/**")
                                .or(path("/api/fares/**")),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireRole(
                        "ROLE_SYSTEM_ADMIN",
                        "ROLE_AIRLINE_OWNER"
                ))
                .filter(lb("pricing-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> seatServiceRoutes() {
        return route("seat-service")
                .route(
                        path("/api/seat/**")
                                .or(path("/api/seat-maps/**"))
                                .or(path("/api/seat-instances/**"))
                                .or(path("/api/flight-inst-cabin/**")),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireAuth())
                .filter(lb("seat-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceRoutes() {
        return route("booking-service")
                .route(
                        path("/api/booking/**"),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireAuth())
                .filter(lb("booking-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoutes() {
        return route("payment-service")
                .route(
                        path("/api/payment/**"),
                        http()
                )
                .filter(jwtAuthenticationFilter.requireAuth())
                .filter(lb("payment-service"))
                .build();
    }

}
