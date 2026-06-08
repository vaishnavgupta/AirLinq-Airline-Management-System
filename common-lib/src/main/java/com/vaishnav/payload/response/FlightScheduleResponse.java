package com.vaishnav.payload.response;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightScheduleResponse {

    private Long id;
    private Long flightId;
    private String flightNumber;

    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<DayOfWeek> operatingDays;

    private Boolean isActive;


}
