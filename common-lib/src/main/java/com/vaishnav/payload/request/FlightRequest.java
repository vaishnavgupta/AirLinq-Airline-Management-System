package com.vaishnav.payload.request;

import com.vaishnav.enums.FlightStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightRequest {

    @NotBlank(message = "Flight Number is required")
    @Size(max = 10)
    private String flightNumber;

    @NotNull(message = "Airline Id is required")
    private Long airlineId;

    @NotNull(message = "Aircraft Id is required")
    private Long aircraftId;

    @NotNull(message = "Departure Airport Id is required")
    private Long departureAirportId;

    @NotNull(message = "Arrival Airport Id is required")
    private Long arrivalAirportId;

    private FlightStatus flightStatus ;

}
