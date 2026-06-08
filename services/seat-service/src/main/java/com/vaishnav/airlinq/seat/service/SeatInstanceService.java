package com.vaishnav.airlinq.seat.service;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.GenerateSeatInstanceRequest;
import com.vaishnav.payload.request.SeatBookRequest;
import com.vaishnav.payload.request.SeatHoldRequest;
import com.vaishnav.payload.response.SeatInstanceResponse;

import java.util.List;

public interface SeatInstanceService {

    List<SeatInstanceResponse> generateSeatInstances(
            Long airlineId,
            GenerateSeatInstanceRequest request
    ) throws Exception;

    SeatInstanceResponse getSeatInstanceById(Long id) throws Exception;

    List<SeatInstanceResponse> getSeatsByFlightInstanceId(Long flightInstanceId) throws Exception;

    List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceId(Long flightInstanceId) throws Exception;

    List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceIdAndCabinClass(
            Long flightInstanceId,
            CabinClass cabinClass
    ) throws Exception;

    SeatInstanceResponse holdSeat(
            Long seatInstanceId,
            SeatHoldRequest request
    ) throws Exception;

    SeatInstanceResponse bookSeat(
            Long seatInstanceId,
            SeatBookRequest request
    ) throws Exception;

    SeatInstanceResponse releaseSeat(Long seatInstanceId) throws Exception;

    SeatInstanceResponse blockSeat(Long seatInstanceId) throws Exception;

    SeatInstanceResponse unblockSeat(Long seatInstanceId) throws Exception;

    void releaseExpiredHeldSeats();

}
