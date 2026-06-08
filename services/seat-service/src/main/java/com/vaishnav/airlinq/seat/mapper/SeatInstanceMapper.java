package com.vaishnav.airlinq.seat.mapper;

import com.vaishnav.airlinq.seat.model.SeatInstance;
import com.vaishnav.payload.response.FlightInstanceResponse;
import com.vaishnav.payload.response.SeatInstanceResponse;

public class SeatInstanceMapper {

    public static SeatInstanceResponse toResponse(
            SeatInstance seatInstance,
            FlightInstanceResponse flightInstanceResponse
    ) {
        if (seatInstance == null) {
            return null;
        }

        return SeatInstanceResponse.builder()
                .id(seatInstance.getId())
                .airlineId(seatInstance.getAirlineId())
                .flightInstance(flightInstanceResponse)
                .seatId(seatInstance.getSeatId())
                .flightInstanceCabinId(seatInstance.getFlightInstanceCabinId())
                .seatNumber(seatInstance.getSeatNumber())
                .rowNumber(seatInstance.getRowNumber())
                .seatColumn(seatInstance.getSeatColumn())
                .cabinClass(seatInstance.getCabinClass())
                .seatType(seatInstance.getSeatType())
                .status(seatInstance.getStatus())
                .bookingId(seatInstance.getBookingId())
                .passengerId(seatInstance.getPassengerId())
                .seatFee(seatInstance.getSeatFee())
                .isExitRow(seatInstance.getIsExitRow())
                .hasExtraLegroom(seatInstance.getHasExtraLegroom())
                .lockedUntil(seatInstance.getLockedUntil())
                .createdAt(seatInstance.getCreatedAt())
                .updatedAt(seatInstance.getUpdatedAt())
                .build();
    }

}
