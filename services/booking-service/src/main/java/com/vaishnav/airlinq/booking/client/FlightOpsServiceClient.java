package com.vaishnav.airlinq.booking.client;

import com.vaishnav.payload.response.FlightInstanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-ops-service")
public interface FlightOpsServiceClient {

    @GetMapping("/api/flight-instance/{id}")
    FlightInstanceResponse getFlightInstanceById(@PathVariable Long id) throws Exception;

}
