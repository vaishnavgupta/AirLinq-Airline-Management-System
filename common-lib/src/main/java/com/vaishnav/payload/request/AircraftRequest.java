package com.vaishnav.payload.request;

import com.vaishnav.enums.AircraftStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftRequest {
    @NotBlank(message = "Aircraft Code is required")
    private String code;

    @NotBlank(message = "Aircraft Model is required")
    private String model;

    @NotBlank(message = "Aircraft Manufacturer is required")
    @Size(min = 5, max = 49, message = "Manufacturer must have 5 - 49 characters")
    private String manufacturer;

    @NotNull(message = "Seating capacity is required")
    @Positive(message = "Seating capacity must be positive")
    private Integer seatingCapacity;

    @NotNull(message = "Economy Seats capacity is required")
    @PositiveOrZero(message = "Economy Seats capacity must be positive or zero")
    private Integer economySeats ;

    @NotNull(message = "    Premium Economy Seats capacity is required")
    @PositiveOrZero(message = "Premium Economy Seats capacity must be positive or zero")
    private Integer premiumEconomySeats ;

    @NotNull(message = "    Business Class Seats capacity is required")
    @PositiveOrZero(message = "Business Seats capacity must be positive or zero")
    private Integer businessSeats ;

    @NotNull(message = "First Class Seats capacity is required")
    @PositiveOrZero(message = "First Class Seats capacity must be positive or zero")
    private Integer firstClassSeats ;

    @Positive(message = "cruising Speed Kmh must be positive")
    private Integer cruisingSpeedKmh;

    private Integer rangeKmh;

    @Positive(message = "Year of Manufacturer must be positive")
    private Integer yearOfManufacturer;

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    @NotNull(message = "Aircraft Status is mandatory")
    private AircraftStatus status;

    @NotNull(message = "Is Available is mandatory")
    private Boolean isAvailable;

    private Long airlineId;

    private Long currentAirportId;

}
