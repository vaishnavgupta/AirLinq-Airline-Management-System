package com.vaishnav.airlinq.payment.client;

import com.vaishnav.payload.request.BookingConfirmRequest;
import com.vaishnav.payload.response.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "booking-service")
public interface BookingServiceClient {

    @GetMapping("/api/booking/{id}")
    BookingResponse getBookingById(@PathVariable Long id) throws Exception;

    @PostMapping("/api/booking/{id}/confirm")
    BookingResponse confirmBooking(@PathVariable Long id, @RequestBody BookingConfirmRequest request) throws Exception;

}
