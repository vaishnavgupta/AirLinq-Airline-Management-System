package com.vaishnav.airlinq.airline.service;

import com.vaishnav.payload.request.AircraftRequest;
import com.vaishnav.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {

    AircraftResponse createAircraft(AircraftRequest request, Long ownerId) throws Exception;
    AircraftResponse getById(Long id) throws Exception;
    List<AircraftResponse> listAllAircraftByOwner(Long ownerId) throws Exception;
    AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId) throws Exception;
    void deleteAircraft(Long id, Long ownerId) throws Exception;

}
