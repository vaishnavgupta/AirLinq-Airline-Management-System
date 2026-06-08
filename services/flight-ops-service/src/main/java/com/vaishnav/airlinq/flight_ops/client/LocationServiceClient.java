package com.vaishnav.airlinq.flight_ops.client;

import com.vaishnav.payload.response.AirportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "location-service")
public interface LocationServiceClient {

    @GetMapping("/api/airport/{id}")
    AirportResponse getAirportById(@PathVariable Long id);

}
