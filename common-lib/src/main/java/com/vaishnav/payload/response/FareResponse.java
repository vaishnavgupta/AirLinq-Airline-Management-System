package com.vaishnav.payload.response;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareResponse {

    private Long id;

    private AirlineResponse airline;

    private FlightResponse flight;

    private FlightInstanceResponse flightInstanceResponse;

    private CabinClass cabinClass;

    private FareType fareType;

    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal serviceFee;

    private BigDecimal totalFare;

    private String currency;

    private Integer availableSeats;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private FareStatus status;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;

}
