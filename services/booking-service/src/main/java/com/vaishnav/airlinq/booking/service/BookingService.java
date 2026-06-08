package com.vaishnav.airlinq.booking.service;

import com.vaishnav.payload.request.BookingConfirmRequest;
import com.vaishnav.payload.request.BookingRequest;
import com.vaishnav.payload.response.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    BookingResponse createBooking(Long userId, BookingRequest  bookingRequest) throws Exception;

    BookingResponse getBookingById(Long id) throws Exception;

    BookingResponse getBookingByReference(String bookingReference) throws Exception;

    Page<BookingResponse> getBookingByUserId(Long userId, Pageable pageable);

    BookingResponse confirmBooking(Long userId, BookingConfirmRequest request) throws Exception;

    BookingResponse cancelBooking(Long id, Long userId) throws Exception;

    void expirePendingBookings() throws Exception;

}
