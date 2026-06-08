package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatHoldRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    private Long passengerId;

    private Integer holdMinutes;
}
