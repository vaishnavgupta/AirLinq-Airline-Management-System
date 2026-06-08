package com.vaishnav.airlinq.booking.scheduler;

import com.vaishnav.airlinq.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void expirePendingBookings() throws Exception {
        bookingService.expirePendingBookings();
    }

}
