package com.vaishnav.payload.response;

import com.vaishnav.enums.CabinClass;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceCabinResponse {
    private Long id;

    private AirlineResponse airline;

    private FlightInstanceResponse flightInstance;

    private CabinClass cabinClass;

    private Integer totalSeats;

    private Integer availableSeats;

    private Integer bookedSeats;

    private Integer blockedSeats;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
