package com.vaishnav.payload.request;

import com.vaishnav.enums.CabinClass;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceCabinRequest {
    private Long airlineId;

    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Total seats is required")
    @PositiveOrZero
    private Integer totalSeats;

    @PositiveOrZero
    private Integer availableSeats;

    @PositiveOrZero
    private Integer bookedSeats;

    @PositiveOrZero
    private Integer blockedSeats;

    private Boolean isActive;
}
