package com.vaishnav.payload.response;

import com.vaishnav.enums.FlightStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightResponse {
    private Long id;

    private String flightNumber;

    private AirlineResponse airline;

    private AircraftResponse aircraft;

    private AirportResponse departureAirport;

    private AirportResponse arrivalAirport;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private Double lowestPrice;

    private Integer totalAvailableSeats;

    private FlightStatus flightStatus ;

    private Instant createdAt;

    private Instant updatedAt;
}
