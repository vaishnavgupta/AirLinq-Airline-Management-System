package com.vaishnav.airlinq.seat.client;

import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "airline-service")
public interface AirlineServiceClient {

    @GetMapping("/api/airline/admin")
    AirlineResponse getAirlineByUserId(
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception;

    @GetMapping("/api/airline/{id}")
    AirlineResponse getAirlineById(
            @PathVariable Long id
    ) throws Exception;

    @GetMapping("/api/aircraft/{id}")
    AircraftResponse getAircraftById(
            @PathVariable Long id
    ) throws Exception;


}
