package com.vaishnav.airlinq.seat.scheduler;

import com.vaishnav.airlinq.seat.service.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatHoldCleanupScheduler {

    private final SeatInstanceService seatInstanceService;

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredSeats() {
        seatInstanceService.releaseExpiredHeldSeats();;
    }

}
