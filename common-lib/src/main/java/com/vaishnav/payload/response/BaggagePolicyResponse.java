package com.vaishnav.payload.response;

import com.vaishnav.enums.BaggageUnit;
import com.vaishnav.enums.CabinClass;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaggagePolicyResponse {

    private Long id;

    private AirlineResponse airline;

    private FareResponse fare;

    private FlightResponse flight;

    private FlightInstanceResponse flightInstance;

    private CabinClass cabinClass;

    private Integer cabinBagAllowance;

    private BaggageUnit cabinBagUnit;

    private BigDecimal cabinBagMaxWeight;

    private Integer checkedBagAllowance;

    private BaggageUnit checkedBagUnit;

    private BigDecimal checkedBagMaxWeight;

    private BigDecimal extraBagFee;

    private BigDecimal overweightFee;

    private String policyDescription;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
