package com.vaishnav.airlinq.pricing.service;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.BaggagePolicyRequest;
import com.vaishnav.payload.response.BaggagePolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaggagePolicyService {

    BaggagePolicyResponse createBaggagePolicy(
            Long airlineId,
            BaggagePolicyRequest request
    ) throws Exception;

    BaggagePolicyResponse getBaggagePolicyById(Long id) throws Exception;

    Page<BaggagePolicyResponse> searchBaggagePolicies(
            Long airlineId,
            Long fareId,
            Long flightId,
            Long flightInstanceId,
            CabinClass cabinClass,
            Pageable pageable
    );

    BaggagePolicyResponse getPolicyForFare(Long fareId) throws Exception;

    BaggagePolicyResponse getPolicyForFlightInstance(
            Long flightInstanceId,
            CabinClass cabinClass
    ) throws Exception;

    BaggagePolicyResponse updateBaggagePolicy(
            Long id,
            BaggagePolicyRequest request
    ) throws Exception;

    void deleteBaggagePolicy(Long airlineId, Long id) throws Exception;
}
