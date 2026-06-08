package com.vaishnav.airlinq.pricing.mapper;

import com.vaishnav.airlinq.pricing.model.Fare;
import com.vaishnav.payload.request.FareRequest;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.FareResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import com.vaishnav.payload.response.FlightResponse;

import java.math.BigDecimal;

public class FareMapper {

    public static Fare toFare(FareRequest request) {
        if(request == null) {
            return null;
        }
        Fare fare = Fare.builder()
                .cabinClass(request.getCabinClass())
                .fareType(request.getFareType())
                .baseFare(request.getBaseFare())
                .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
                .serviceFee(request.getServiceFee() != null ? request.getServiceFee() : BigDecimal.valueOf(100))
                .currency(request.getCurrency())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .status(request.getStatus())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        fare.setTotalFare(fare.getAddedTotalFare());
        return fare;
    }

    public static FareResponse toFareResponse(
            Fare fare,
            AirlineResponse airline,
            FlightResponse flight,
            FlightInstanceResponse flightInstanceResponse
    ) {
        if(fare == null) {
            return null;
        }
        return FareResponse.builder()
                .airline(airline)
                .flight(flight)
                .flightInstanceResponse(flightInstanceResponse)
                .cabinClass(fare.getCabinClass())
                .fareType(fare.getFareType())
                .baseFare(fare.getBaseFare())
                .taxAmount(fare.getTaxAmount())
                .serviceFee(fare.getServiceFee())
                .totalFare(fare.getTotalFare())
                .id(fare.getId())
                .currency(fare.getCurrency())
                .availableSeats(fare.getAvailableSeats())
                .validFrom(fare.getValidFrom())
                .validTo(fare.getValidTo())
                .status(fare.getStatus())
                .isActive(fare.getIsActive())
                .createdAt(fare.getCreatedAt())
                .updatedAt(fare.getUpdatedAt())
                .build();
    }

    public static void updateFare(Fare fare, FareRequest request) {
        if (fare == null || request == null) return;

        if (request.getCabinClass() != null)       fare.setCabinClass(request.getCabinClass());
        if (request.getFareType() != null)         fare.setFareType(request.getFareType());
        if (request.getBaseFare() != null)         fare.setBaseFare(request.getBaseFare());
        if (request.getTaxAmount() != null)        fare.setTaxAmount(request.getTaxAmount());
        if (request.getServiceFee() != null)       fare.setServiceFee(request.getServiceFee());
        if (request.getCurrency() != null)         fare.setCurrency(request.getCurrency());
        if (request.getAvailableSeats() != null)   fare.setAvailableSeats(request.getAvailableSeats());
        if (request.getValidFrom() != null)        fare.setValidFrom(request.getValidFrom());
        if (request.getValidTo() != null)          fare.setValidTo(request.getValidTo());
        if (request.getStatus() != null)           fare.setStatus(request.getStatus());
        if (request.getIsActive() != null)         fare.setIsActive(request.getIsActive());

        // Recalculate totalFare if any pricing field was updated
        if (request.getBaseFare() != null || request.getTaxAmount() != null || request.getServiceFee() != null) {
            BigDecimal base    = fare.getBaseFare()    != null ? fare.getBaseFare()    : BigDecimal.ZERO;
            BigDecimal tax     = fare.getTaxAmount()   != null ? fare.getTaxAmount()   : BigDecimal.ZERO;
            BigDecimal service = fare.getServiceFee()  != null ? fare.getServiceFee()  :  BigDecimal.valueOf(100);
            fare.setTotalFare(base.add(tax).add(service));
        }
    }


}
