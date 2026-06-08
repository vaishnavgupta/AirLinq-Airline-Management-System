package com.vaishnav.airlinq.pricing.service.impl;

import com.vaishnav.airlinq.pricing.client.AirlineServiceClient;
import com.vaishnav.airlinq.pricing.client.FlightOpsServiceClient;
import com.vaishnav.airlinq.pricing.mapper.BaggagePolicyMapper;
import com.vaishnav.airlinq.pricing.model.BaggagePolicy;
import com.vaishnav.airlinq.pricing.repository.BaggagePolicyRepository;
import com.vaishnav.airlinq.pricing.service.BaggagePolicyService;
import com.vaishnav.airlinq.pricing.service.FareService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.BaggagePolicyRequest;
import com.vaishnav.payload.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaggagePolicyServiceImpl implements BaggagePolicyService {

    private final BaggagePolicyRepository baggagePolicyRepository;
    private final AirlineServiceClient airlineServiceClient;
    private final FlightOpsServiceClient  flightOpsServiceClient;
    private final FareService fareService;

    @Override
    public BaggagePolicyResponse createBaggagePolicy(
            Long userId,
            BaggagePolicyRequest request
    ) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);
        if(airlineResponse==null || airlineResponse.getId()==null){
            throw new Exception("Airline not found with userId");
        }
        if(!request.getAirlineId().equals(airlineResponse.getId())){
            throw new Exception("Airline Id not matched with provided user id");
        }

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(request.getFlightInstanceId());
        if(flightInstanceResponse==null || flightInstanceResponse.getId()==null){
            throw new Exception("Flight instance not found with provided id");
        }
        if(!flightInstanceResponse.getAirlineId().equals(airlineResponse.getId())){
            throw new Exception("Airline Id not matched with provided airline id");
        }

        FlightResponse flightResponse = flightOpsServiceClient.getFlight(flightInstanceResponse.getFlightId());
        if(flightResponse==null || flightResponse.getId()==null){
            throw new Exception("Flight not found with provided id");
        }

        FareResponse fareResponse = fareService.getFareById(request.getFareId());

        BaggagePolicy baggagePolicy = BaggagePolicyMapper.toBaggagePolicy(request);
        baggagePolicy.setAirlineId(airlineResponse.getId());
        baggagePolicy.setFlightId(flightResponse.getId());
        baggagePolicy.setFlightInstanceId(flightInstanceResponse.getId());
        baggagePolicy.setFareId(fareResponse.getId());

        baggagePolicy = baggagePolicyRepository.save(baggagePolicy);
        return toResponse(baggagePolicy,airlineResponse,fareResponse,flightResponse,flightInstanceResponse);
    }

    @Override
    public BaggagePolicyResponse getBaggagePolicyById(Long id) throws Exception {
        BaggagePolicy baggagePolicy = baggagePolicyRepository.findById(id)
                .orElseThrow(() -> new Exception("Baggage policy not found with id: " + id));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(baggagePolicy.getAirlineId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(baggagePolicy.getFlightInstanceId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(baggagePolicy.getFlightId());
        FareResponse fareResponse = fareService.getFareById(baggagePolicy.getFareId());

        return toResponse(baggagePolicy,airlineResponse,fareResponse,flightResponse,flightInstanceResponse);
    }

    @Override
    public Page<BaggagePolicyResponse> searchBaggagePolicies(
            Long airlineId,
            Long fareId,
            Long flightId,
            Long flightInstanceId,
            CabinClass cabinClass,
            Pageable pageable
    ) {
        Page<BaggagePolicy> baggagePolicies = baggagePolicyRepository.searchBaggagePolicies(
                        airlineId,
                        fareId,
                        flightId,
                        flightInstanceId,
                        cabinClass,
                        pageable
                );
        return convertPageToBaggagePolicyResponse(baggagePolicies);
    }

    @Override
    public BaggagePolicyResponse getPolicyForFare(Long fareId) throws Exception {
        FareResponse fareResponse = fareService.getFareById(fareId);

        BaggagePolicy baggagePolicy = baggagePolicyRepository.findByFareIdAndIsActiveTrue(fareId)
                .orElseThrow(() -> new Exception("Baggage policy not found for fare id: " + fareId));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(baggagePolicy.getAirlineId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(baggagePolicy.getFlightInstanceId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(baggagePolicy.getFlightId());

        return toResponse(baggagePolicy,airlineResponse,fareResponse,flightResponse,flightInstanceResponse);
    }

    @Override
    public BaggagePolicyResponse getPolicyForFlightInstance(
            Long flightInstanceId,
            CabinClass cabinClass
    ) throws Exception {
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(flightInstanceId);

        BaggagePolicy baggagePolicy = baggagePolicyRepository
                .findFirstByFlightInstanceIdAndCabinClassAndIsActiveTrue(flightInstanceId, cabinClass)
                .orElseThrow(() -> new Exception(
                        "Baggage policy not found for flight instance id: " + flightInstanceId
                ));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(baggagePolicy.getAirlineId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(baggagePolicy.getFlightId());
        FareResponse fareResponse = fareService.getFareById(baggagePolicy.getFareId());

        return toResponse(baggagePolicy,airlineResponse,fareResponse,flightResponse,flightInstanceResponse);
    }

    @Override
    public BaggagePolicyResponse updateBaggagePolicy(
            Long id,
            BaggagePolicyRequest request
    ) throws Exception {
        BaggagePolicy baggagePolicy = baggagePolicyRepository.findById(id)
                .orElseThrow(() -> new Exception("Baggage policy not found with id: " + id));
        BaggagePolicyMapper.updateBaggagePolicy(baggagePolicy, request);
        baggagePolicy = baggagePolicyRepository.save(baggagePolicy);

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(baggagePolicy.getAirlineId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(baggagePolicy.getFlightInstanceId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(baggagePolicy.getFlightId());
        FareResponse fareResponse = fareService.getFareById(baggagePolicy.getFareId());

        return toResponse(baggagePolicy,airlineResponse,fareResponse,flightResponse,flightInstanceResponse);
    }

    @Override
    public void deleteBaggagePolicy(Long userId, Long id) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);
        if(airlineResponse==null || airlineResponse.getId()==null){
            throw new Exception("Airline not found with userId");
        }
        BaggagePolicy baggagePolicy = baggagePolicyRepository.findByIdAndAirlineId(id, airlineResponse.getId())
                .orElseThrow(() -> new Exception("Baggage policy not found with id: " + id));

        baggagePolicy.setIsActive(false);
        baggagePolicyRepository.save(baggagePolicy);
    }

    private BaggagePolicyResponse toResponse(
            BaggagePolicy baggagePolicy,
            AirlineResponse airline,
            FareResponse fare,
            FlightResponse flight,
            FlightInstanceResponse flightInstance
            ) {
        return BaggagePolicyMapper.toBaggagePolicyResponse(
                baggagePolicy,
                airline,
                fare,
                flight,
                flightInstance
        );
    }

    private Page<BaggagePolicyResponse> convertPageToBaggagePolicyResponse(
            Page<BaggagePolicy> page
    ) {

        Map<Long, AirlineResponse> airlineCache = new HashMap<>();
        Map<Long, FareResponse> fareCache = new HashMap<>();
        Map<Long, FlightResponse> flightCache = new HashMap<>();
        Map<Long, FlightInstanceResponse> flightInstanceCache = new HashMap<>();

        List<BaggagePolicyResponse> responseList = page.getContent()
                .stream()
                .map(bp -> {

                    AirlineResponse airline = airlineCache.computeIfAbsent(
                            bp.getAirlineId(),
                            id -> {
                                try {
                                    return airlineServiceClient.getAirlineById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch airline: " + id, e);
                                }
                            }
                    );

                    FareResponse fare = fareCache.computeIfAbsent(
                            bp.getFareId(),
                            id -> {
                                try {
                                    return fareService.getFareById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch fare: " + id, e);
                                }
                            }
                    );

                    FlightResponse flight = flightCache.computeIfAbsent(
                            bp.getFlightId(),
                            id -> {
                                try {
                                    return flightOpsServiceClient.getFlight(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch flight: " + id, e);
                                }
                            }
                    );

                    FlightInstanceResponse flightInstance = flightInstanceCache.computeIfAbsent(
                            bp.getFlightInstanceId(),
                            id -> {
                                try {
                                    return flightOpsServiceClient.getFlightInstance(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch flightInstance: " + id, e);
                                }
                            }
                    );

                    return toResponse(
                            bp,
                            airline,
                            fare,
                            flight,
                            flightInstance
                    );
                })
                .toList();

        return new PageImpl<>(
                responseList,
                page.getPageable(),
                page.getTotalElements()
        );
    }

}
