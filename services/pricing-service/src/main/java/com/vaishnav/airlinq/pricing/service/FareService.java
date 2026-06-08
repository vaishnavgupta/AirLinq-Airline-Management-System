package com.vaishnav.airlinq.pricing.service;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import com.vaishnav.payload.request.FareRequest;
import com.vaishnav.payload.response.FareResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FareService {

    FareResponse createFare(Long userId, FareRequest request) throws Exception;

    FareResponse getFareById(Long id) throws Exception;

    Page<FareResponse> searchFares(
            Long airlineId,
            Long flightId,
            Long flightInstanceId,
            CabinClass cabinClass,
            FareType fareType,
            FareStatus status,
            Pageable pageable
    );

    FareResponse updateFare(Long id, FareRequest request) throws Exception;

    void deleteFare(Long airlineId, Long id) throws Exception;

    FareResponse getLowestFareByFlightId(Long flightId) throws Exception;

    List<FareResponse> getFaresByFlightId(Long flightId) throws Exception;

}
