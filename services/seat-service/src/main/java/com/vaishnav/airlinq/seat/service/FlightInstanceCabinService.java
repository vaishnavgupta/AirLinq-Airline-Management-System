package com.vaishnav.airlinq.seat.service;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.response.FlightInstanceCabinResponse;

import java.util.List;

public interface FlightInstanceCabinService {
    List<FlightInstanceCabinResponse> getCabinsByFlightInstanceId(Long flightInstanceId);

    FlightInstanceCabinResponse getCabinByFlightInstanceIdAndCabinClass(
            Long flightInstanceId,
            CabinClass cabinClass
    ) throws Exception;

    void increaseBookedSeats(Long flightInstanceId, CabinClass cabinClass) throws Exception;

    void releaseBookedSeat(Long flightInstanceId, CabinClass cabinClass) throws Exception;
}
