package com.vaishnav.payload.request;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareRequest {

    @NotNull(message = "Airline Id is required")
    private Long airlineId;

    @NotNull(message = "Flight Id is required")
    private Long flightId;

    @NotNull(message = "Flight Instance Id is required")
    private Long flightInstanceId;

    @NotNull(message = "Cabin Class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Fare Type is required")
    private FareType fareType;

    @NotNull(message = "Base fare is required")
    @Positive
    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal serviceFee;

    @NotBlank(message = "Currency is required")
    private String currency;

    private Integer availableSeats;

    @NotNull(message = "Valid From is required")
    private LocalDateTime validFrom;

    @NotNull(message = "Valid to is required")
    private LocalDateTime validTo;

    @NotNull(message = "fare status is required")
    private FareStatus status;

    private Boolean isActive ;
}
