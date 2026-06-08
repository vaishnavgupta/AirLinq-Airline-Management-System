package com.vaishnav.payload.request;

import com.vaishnav.enums.BaggageUnit;
import com.vaishnav.enums.CabinClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaggagePolicyRequest {

    private Long airlineId;

    private Long fareId;

    private Long flightId;

    private Long flightInstanceId;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Cabin bag allowance is required")
    @PositiveOrZero
    private Integer cabinBagAllowance;

    @NotNull(message = "Cabin bag unit is required")
    private BaggageUnit cabinBagUnit;

    @PositiveOrZero
    private BigDecimal cabinBagMaxWeight;

    @NotNull(message = "Checked bag allowance is required")
    @PositiveOrZero
    private Integer checkedBagAllowance;

    @NotNull(message = "Checked bag unit is required")
    private BaggageUnit checkedBagUnit;

    @PositiveOrZero
    private BigDecimal checkedBagMaxWeight;

    @PositiveOrZero
    private BigDecimal extraBagFee;

    @PositiveOrZero
    private BigDecimal overweightFee;

    private String policyDescription;

    private Boolean isActive;
}
