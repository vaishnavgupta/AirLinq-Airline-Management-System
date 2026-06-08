package com.vaishnav.airlinq.seat.mapper;

import com.vaishnav.airlinq.seat.model.SeatMap;
import com.vaishnav.payload.request.SeatMapRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.SeatMapResponse;

public class SeatMapMapper {

    public static SeatMap toSeatMap(SeatMapRequest  request) {
        if(request == null) {
            return null;
        }

        return SeatMap.builder()
                .airlineId(request.getAirlineId())
                .aircraftId(request.getAircraftId())
                .name(request.getName())
                .totalRows(request.getTotalRows())
                .totalSeats(request.getTotalSeats())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

    }

    public static SeatMapResponse toResponse(
            SeatMap map,
            AirlineResponse airlineResponse,
            AircraftResponse aircraftResponse
    ) {
        if(map == null) {
            return null;
        }

        return SeatMapResponse.builder()
                .id(map.getId())
                .airline(airlineResponse)
                .aircraft(aircraftResponse)
                .name(map.getName())
                .totalRows(map.getTotalRows())
                .totalSeats(map.getTotalSeats())
                .isActive(map.getIsActive())
                .createdAt(map.getCreatedAt())
                .updatedAt(map.getUpdatedAt())
                .build();
    }

    public static void updateSeatMap(SeatMap seatMap, SeatMapRequest request) {
        if (seatMap == null || request == null) return;

        if (request.getName() != null)        seatMap.setName(request.getName());
        if (request.getTotalRows() != null)   seatMap.setTotalRows(request.getTotalRows());
        if (request.getTotalSeats() != null)  seatMap.setTotalSeats(request.getTotalSeats());
        if (request.getIsActive() != null)    seatMap.setIsActive(request.getIsActive());
    }

}
