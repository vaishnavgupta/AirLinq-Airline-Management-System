package com.vaishnav.payload.response;

import com.vaishnav.enums.AircraftStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {

    private Long id;
    private String code;
    private String model;
    private String manufacturer;
    private Integer seatingCapacity;
    private Integer economySeats;
    private Integer premiumEconomySeats;
    private Integer businessSeats;
    private Integer firstClassSeats;
    private Integer rangeKm;
    private Integer cruisingSpeedKmh;
    private Integer yearOfManufacture;
    private LocalDate registrationDate;
    private LocalDate nextMaintenanceDate;
    private AircraftStatus status;
    private Boolean isAvailable;

    private Long airlineId;
    private String airlineName;
    private String airlineIataCode;

    private Long currentAirportId;
    private Long currentAirportCity;
    private String currentAirportCode;
    private String currentAirportName;

    private Integer totalSeats;
    private Boolean requiresMaintenance;
    private Boolean isOperational;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
