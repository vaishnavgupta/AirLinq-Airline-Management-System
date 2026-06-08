package com.vaishnav.airlinq.location.service;

import com.vaishnav.payload.request.AirportRequest;
import com.vaishnav.payload.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest request);

    AirportResponse getAirportById(Long id);

    List<AirportResponse> getAllAirports();

    AirportResponse updateAirport(Long id, AirportRequest request);

    void deleteAirportById(Long id);

    List<AirportResponse> getAirportsByCityId(Long cityId);

}
