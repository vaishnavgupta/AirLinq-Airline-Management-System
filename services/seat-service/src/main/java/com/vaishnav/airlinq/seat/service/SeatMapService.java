package com.vaishnav.airlinq.seat.service;

import com.vaishnav.payload.request.SeatMapRequest;
import com.vaishnav.payload.response.SeatMapResponse;

import java.util.List;

public interface SeatMapService {

    SeatMapResponse createSeatMap(Long airlineId, SeatMapRequest request) throws Exception;

    SeatMapResponse getSeatMapById(Long id) throws Exception;

    List<SeatMapResponse> getSeatMapsByAirlineId(Long airlineId);

    List<SeatMapResponse> getSeatMapsByAircraftId(Long aircraftId);

    SeatMapResponse updateSeatMap(Long id, SeatMapRequest request) throws Exception;

    void deleteSeatMap(Long airlineId, Long id) throws Exception;

}
