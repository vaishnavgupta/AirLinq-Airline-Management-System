package com.vaishnav.airlinq.seat.service;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.SeatRequest;
import com.vaishnav.payload.response.SeatResponse;

import java.util.List;

public interface SeatService {

    SeatResponse createSeat(SeatRequest seatRequest) throws Exception;

    SeatResponse getSeatById(Long seatId) throws Exception;

    List<SeatResponse> getSeatsBySeatMapId(Long  seatMapId);

    List<SeatResponse> getSeatsBySeatMapIdAndCabinClass(Long seatMapId, CabinClass cabinClass);

    SeatResponse updateSeat(Long id, SeatRequest request) throws Exception;

    void deleteSeat(Long id) throws Exception;

}
