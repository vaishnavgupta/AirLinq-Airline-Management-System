package com.vaishnav.payload.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceSearchRequest {
    private Long airlineId;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private Long flightId;
    private LocalDateTime onDate;
}
