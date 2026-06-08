package com.vaishnav.payload.request;

import com.vaishnav.enums.FlightStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceRequest {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    private Long airlineId;

    private Long scheduleId;

    private Long departureAirportId;

    private Long arrivalAirportId;

    @NotNull(message = "Departure date-time is required")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date-time iş required")
    private LocalDateTime arrivalDateTime;

    @NotNull(message = "Total seats is required")
    @Positive
    private Integer totalSeats;

    @PositiveOrZero
    private Integer availableSeats;

    private FlightStatus status;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;
    private Boolean isActive;

    private String terminal;
    private String gate;

}
