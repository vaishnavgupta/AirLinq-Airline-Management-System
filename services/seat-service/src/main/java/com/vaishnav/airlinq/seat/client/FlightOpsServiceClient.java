package com.vaishnav.airlinq.seat.client;

import com.vaishnav.payload.response.FlightInstanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-ops-service")
public interface FlightOpsServiceClient {

    @GetMapping("/api/flight-instance/{id}")
    FlightInstanceResponse getFlightInstance(@PathVariable Long id);

}
