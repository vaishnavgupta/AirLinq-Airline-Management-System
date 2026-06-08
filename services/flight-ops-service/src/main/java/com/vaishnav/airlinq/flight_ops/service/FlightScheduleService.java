package com.vaishnav.airlinq.flight_ops.service;

import com.vaishnav.payload.request.FlightScheduleRequest;
import com.vaishnav.payload.response.FlightScheduleResponse;

import java.util.List;

public interface FlightScheduleService {

    FlightScheduleResponse createFlightSchedule(Long userId,
                                                FlightScheduleRequest flightScheduleRequest) throws Exception;

    FlightScheduleResponse getFlightScheduleById(Long id) throws Exception;

    List<FlightScheduleResponse> getFlightScheduleByAirline(Long userId) throws Exception;

    FlightScheduleResponse updateFlightSchedule(Long id,
                                                FlightScheduleRequest flightScheduleRequest) throws Exception;

    void deleteFlightSchedule(Long id) throws Exception;


}
