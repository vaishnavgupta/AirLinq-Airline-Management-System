package com.vaishnav.airlinq.booking.controller;

import com.vaishnav.airlinq.booking.service.BookingService;
import com.vaishnav.payload.request.BookingConfirmRequest;
import com.vaishnav.payload.request.BookingRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BookingRequest bookingRequest
    ) throws Exception {
        return bookingService.createBooking(userId, bookingRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse getBookingById(@PathVariable Long id) throws Exception {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/reference/{bookingReference}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse getBookingByReference(@PathVariable String bookingReference) throws Exception {
        return bookingService.getBookingByReference(bookingReference);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookingResponse> getBookingsByUserId(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable
    ) {
        return bookingService.getBookingByUserId(userId, pageable);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse confirmBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingConfirmRequest request
    ) throws Exception {
        return bookingService.confirmBooking(id, request);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return bookingService.cancelBooking(id, userId);
    }

    @PostMapping("/expire-pending")
    public ResponseEntity<ApiResponse> expirePendingBookings() throws Exception {
        bookingService.expirePendingBookings();
        return ResponseEntity.ok(
                new ApiResponse("Expired pending bookings processed successfully", true)
        );
    }

}