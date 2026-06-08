package com.vaishnav.airlinq.booking.client;

import com.vaishnav.payload.response.BaggagePolicyResponse;
import com.vaishnav.payload.response.FareResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pricing-service")
public interface PricingServiceClient {

    @GetMapping("/api/fares/{id}")
    FareResponse getFareById(@PathVariable Long id) throws Exception ;

    @GetMapping("/api/baggage-policies/fare/{fareId}")
    BaggagePolicyResponse getBaggagePolicyByFare(@PathVariable Long fareId) throws Exception;

}
