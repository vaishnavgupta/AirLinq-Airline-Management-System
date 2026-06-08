package com.vaishnav.airlinq.airline.service;

import com.vaishnav.enums.AirlineStatus;
import com.vaishnav.payload.request.AirlineRequest;
import com.vaishnav.payload.response.AirlineDropDownItem;
import com.vaishnav.payload.response.AirlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AirlineService {
    AirlineResponse createAirline(AirlineRequest request, Long ownerId) throws Exception;
    AirlineResponse getAirlineByOwner(Long ownerId) throws Exception;
    AirlineResponse getAirlineById(Long id) throws Exception;
    Page<AirlineResponse> getAllAirlines(Pageable pageable);
    AirlineResponse updateAirline(AirlineRequest request, Long ownerId) throws Exception;
    void deleteAirline(Long id, Long ownerId) throws Exception;

    AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) throws Exception;

    List<AirlineDropDownItem> getAirlineDropdown();
}
