package com.vaishnav.payload.response;

import com.vaishnav.enums.FlightStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceResponse {

    private Long id;

    private Long flightId;
    private String flightNumber;

    private Long airlineId;
    private String airlineName;
    private String airlineLogo;
    private Long aircraftId;
    private String aircraftModal;
    private String aircraftCode;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String formattedDuration;

    private Integer totalSeats;
    private Integer availableSeats;

    private FlightStatus status;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;
    private Boolean isActive;

    private String terminal;
    private String gate;

    //private FareResponse fare;

}
