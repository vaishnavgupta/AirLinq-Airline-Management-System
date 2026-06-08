package com.vaishnav.airlinq.booking.client;

import com.vaishnav.payload.request.SeatBookRequest;
import com.vaishnav.payload.request.SeatHoldRequest;
import com.vaishnav.payload.response.SeatInstanceResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seat-service")
public interface SeatServiceClient {

    @GetMapping("/api/seat-instances/{id}")
    SeatInstanceResponse getSeatInstanceById(@PathVariable Long id) throws Exception;

    @PostMapping("/api/seat-instances/{id}/hold")
    SeatInstanceResponse holdSeat(@PathVariable Long id, @Valid @RequestBody SeatHoldRequest seatHoldRequest) throws Exception;

    @PostMapping("/api/seat-instances/{id}/book")
    SeatInstanceResponse bookSeat(@PathVariable Long id, @Valid @RequestBody SeatBookRequest seatBookRequest) throws Exception;

    @PostMapping("/api/seat-instances/{id}/release")
    SeatInstanceResponse releaseSeat(@PathVariable Long id) throws Exception;

}
