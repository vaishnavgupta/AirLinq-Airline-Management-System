package com.vaishnav.airlinq.pricing.client;

import com.vaishnav.payload.response.FlightInstanceResponse;
import com.vaishnav.payload.response.FlightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-ops-service")
public interface FlightOpsServiceClient {

    @GetMapping("/api/flight-instance/{id}")
    FlightInstanceResponse getFlightInstance(@PathVariable Long id) throws Exception;


    @GetMapping("/api/flight/{id}")
    FlightResponse getFlight(@PathVariable Long id) throws Exception;

}
