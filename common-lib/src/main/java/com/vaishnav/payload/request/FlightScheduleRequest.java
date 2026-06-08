package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotNull;
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
public class FlightScheduleRequest {

    @NotNull(message = "Flight Id is required")
    private Long flightId;

    @NotNull(message = "departure Time is required")
    private LocalTime departureTime;

    @NotNull(message = "arrivalTime is required")
    private LocalTime arrivalTime;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @NotNull(message = "endDate is required")
    private LocalDate endDate;

    @NotNull(message = "openingDays is required")
    private List<DayOfWeek> openingDays;

    private Boolean isActive;

}
