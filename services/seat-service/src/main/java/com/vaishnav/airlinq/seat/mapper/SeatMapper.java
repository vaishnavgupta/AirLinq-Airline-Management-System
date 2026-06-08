package com.vaishnav.airlinq.seat.mapper;

import com.vaishnav.airlinq.seat.model.Seat;
import com.vaishnav.airlinq.seat.model.SeatMap;
import com.vaishnav.payload.request.SeatRequest;
import com.vaishnav.payload.response.SeatResponse;

public class SeatMapper {

    public static Seat toSeat(SeatRequest request, SeatMap seatMap) {
        if(request == null || seatMap == null) return null;
        return Seat.builder()
                .seatMap(seatMap)
                .seatNumber(request.getSeatNumber())
                .rowNumber(request.getRowNumber())
                .seatColumn(request.getSeatColumn())
                .cabinClass(request.getCabinClass())
                .seatType(request.getSeatType())
                .isExitRow(request.getIsExitRow() != null ? request.getIsExitRow() : false)
                .hasExtraLegroom(request.getHasExtraLegroom() != null ? request.getHasExtraLegroom() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : false)
                .build();
    }

    public static SeatResponse toResponse(Seat seat) {
        if(seat == null) return null;
        return SeatResponse.builder()
                .id(seat.getId())
                .seatMapId(seat.getSeatMap().getId())
                .seatNumber(seat.getSeatNumber())
                .rowNumber(seat.getRowNumber())
                .seatColumn(seat.getSeatColumn())
                .cabinClass(seat.getCabinClass())
                .seatType(seat.getSeatType())
                .isExitRow(seat.getIsExitRow())
                .hasExtraLegroom(seat.getHasExtraLegroom())
                .isActive(seat.getIsActive())
                .build();
    }

    public static void updateEntity(Seat seat, SeatRequest request) {
        if (seat == null || request == null) return;

        if (request.getCabinClass() != null)      seat.setCabinClass(request.getCabinClass());
        if (request.getSeatType() != null)        seat.setSeatType(request.getSeatType());
        if (request.getIsExitRow() != null)       seat.setIsExitRow(request.getIsExitRow());
        if (request.getHasExtraLegroom() != null) seat.setHasExtraLegroom(request.getHasExtraLegroom());
        if (request.getIsActive() != null)        seat.setIsActive(request.getIsActive());
    }

}
