package com.vaishnav.airlinq.pricing.mapper;

import com.vaishnav.airlinq.pricing.model.BaggagePolicy;
import com.vaishnav.payload.request.BaggagePolicyRequest;
import com.vaishnav.payload.response.*;

import java.math.BigDecimal;

public class BaggagePolicyMapper {

    public static BaggagePolicy toBaggagePolicy(BaggagePolicyRequest request) {
        if (request == null) {
            return null;
        }

        return BaggagePolicy.builder()
                .cabinClass(request.getCabinClass())
                .cabinBagAllowance(request.getCabinBagAllowance())
                .cabinBagUnit(request.getCabinBagUnit())
                .cabinBagMaxWeight(request.getCabinBagMaxWeight())
                .checkedBagAllowance(request.getCheckedBagAllowance())
                .checkedBagUnit(request.getCheckedBagUnit())
                .checkedBagMaxWeight(request.getCheckedBagMaxWeight())
                .extraBagFee(request.getExtraBagFee() != null ? request.getExtraBagFee() : BigDecimal.ZERO)
                .overweightFee(request.getOverweightFee() != null ? request.getOverweightFee() : BigDecimal.ZERO)
                .policyDescription(request.getPolicyDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public static BaggagePolicyResponse toBaggagePolicyResponse(
            BaggagePolicy baggagePolicy,
            AirlineResponse airline,
            FareResponse fare,
            FlightResponse flight,
            FlightInstanceResponse flightInstance
    ) {
        if (baggagePolicy == null) {
            return null;
        }

        return BaggagePolicyResponse.builder()
                .id(baggagePolicy.getId())
                .airline(airline)
                .fare(fare)
                .flight(flight)
                .flightInstance(flightInstance)
                .cabinClass(baggagePolicy.getCabinClass())
                .cabinBagAllowance(baggagePolicy.getCabinBagAllowance())
                .cabinBagUnit(baggagePolicy.getCabinBagUnit())
                .cabinBagMaxWeight(baggagePolicy.getCabinBagMaxWeight())
                .checkedBagAllowance(baggagePolicy.getCheckedBagAllowance())
                .checkedBagUnit(baggagePolicy.getCheckedBagUnit())
                .checkedBagMaxWeight(baggagePolicy.getCheckedBagMaxWeight())
                .extraBagFee(baggagePolicy.getExtraBagFee())
                .overweightFee(baggagePolicy.getOverweightFee())
                .policyDescription(baggagePolicy.getPolicyDescription())
                .isActive(baggagePolicy.getIsActive())
                .createdAt(baggagePolicy.getCreatedAt())
                .updatedAt(baggagePolicy.getUpdatedAt())
                .build();
    }

    public static void updateBaggagePolicy(BaggagePolicy baggagePolicy, BaggagePolicyRequest request) {
        if (baggagePolicy == null || request == null) {
            return;
        }

        if (request.getCabinClass() != null) baggagePolicy.setCabinClass(request.getCabinClass());
        if (request.getCabinBagAllowance() != null) baggagePolicy.setCabinBagAllowance(request.getCabinBagAllowance());
        if (request.getCabinBagUnit() != null) baggagePolicy.setCabinBagUnit(request.getCabinBagUnit());
        if (request.getCabinBagMaxWeight() != null) baggagePolicy.setCabinBagMaxWeight(request.getCabinBagMaxWeight());
        if (request.getCheckedBagAllowance() != null) baggagePolicy.setCheckedBagAllowance(request.getCheckedBagAllowance());
        if (request.getCheckedBagUnit() != null) baggagePolicy.setCheckedBagUnit(request.getCheckedBagUnit());
        if (request.getCheckedBagMaxWeight() != null) baggagePolicy.setCheckedBagMaxWeight(request.getCheckedBagMaxWeight());
        if (request.getExtraBagFee() != null) baggagePolicy.setExtraBagFee(request.getExtraBagFee());
        if (request.getOverweightFee() != null) baggagePolicy.setOverweightFee(request.getOverweightFee());
        if (request.getPolicyDescription() != null) baggagePolicy.setPolicyDescription(request.getPolicyDescription());
        if (request.getIsActive() != null) baggagePolicy.setIsActive(request.getIsActive());
    }
}
