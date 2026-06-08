package com.vaishnav.airlinq.flight_ops.service;

import com.vaishnav.payload.request.FlightInstanceRequest;
import com.vaishnav.payload.response.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface FlightInstanceService {

    FlightInstanceResponse createFlightInstance(
            Long userId,
            FlightInstanceRequest request
    ) throws Exception;

    FlightInstanceResponse getFlightInstanceById(Long id) throws Exception;

    Page<FlightInstanceResponse> getByAirlineId(Long airlineId,
                                                Long departureAirportId,
                                                Long arrivalAirportId,
                                                Long flightId,
                                                LocalDateTime onDate,
                                                Pageable pageable);

    FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest request) throws Exception;

    void deleteFlightInstance(Long id) throws Exception;

}
