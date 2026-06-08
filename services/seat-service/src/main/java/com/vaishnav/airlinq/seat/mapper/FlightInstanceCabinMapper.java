package com.vaishnav.airlinq.seat.mapper;

import com.vaishnav.airlinq.seat.model.FlightInstanceCabin;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.FlightInstanceCabinResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;

public class FlightInstanceCabinMapper {

    public static FlightInstanceCabinResponse toResponse(
            FlightInstanceCabin flightInstanceCabin,
            AirlineResponse airline,
            FlightInstanceResponse flightInstanceResponse
    ) {
        if(flightInstanceCabin == null) return null;

        return FlightInstanceCabinResponse.builder()
                .id(flightInstanceCabin.getId())
                .airline(airline)
                .flightInstance(flightInstanceResponse)
                .cabinClass(flightInstanceCabin.getCabinClass())
                .totalSeats(flightInstanceCabin.getTotalSeats())
                .availableSeats(flightInstanceCabin.getAvailableSeats())
                .bookedSeats(flightInstanceCabin.getBookedSeats())
                .blockedSeats(flightInstanceCabin.getBlockedSeats())
                .isActive(flightInstanceCabin.getIsActive())
                .createdAt(flightInstanceCabin.getCreatedAt())
                .updatedAt(flightInstanceCabin.getUpdatedAt())
                .build();
    }

}
