package com.vaishnav.airlinq.pricing.service.impl;

import com.vaishnav.airlinq.pricing.client.AirlineServiceClient;
import com.vaishnav.airlinq.pricing.client.FlightOpsServiceClient;
import com.vaishnav.airlinq.pricing.mapper.FareMapper;
import com.vaishnav.airlinq.pricing.model.Fare;
import com.vaishnav.airlinq.pricing.repository.FareRepository;
import com.vaishnav.airlinq.pricing.service.FareService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import com.vaishnav.payload.request.FareRequest;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.FareResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import com.vaishnav.payload.response.FlightResponse;
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
public class FareServiceImpl implements FareService {
    private final FareRepository fareRepository;
    private final AirlineServiceClient airlineServiceClient;
    private final FlightOpsServiceClient flightOpsServiceClient;

    @Override
    public FareResponse createFare(Long userId, FareRequest request) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);
        if(airlineResponse == null || airlineResponse.getId()==null) {
            throw new Exception("Airline not found with user id: " + userId);
        }
        if(!airlineResponse.getId().equals(request.getAirlineId())) {
            throw new Exception("Airline id not matched with provided id: " + request.getAirlineId());
        }

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(request.getFlightInstanceId());
        if(flightInstanceResponse == null || flightInstanceResponse.getId()==null) {
            throw new Exception("Flight instance not found with id" );
        }
        if(!flightInstanceResponse.getAirlineId().equals(airlineResponse.getId())) {
            throw new Exception("Airline id not matched with flight instance airline id");
        }

        FlightResponse flightResponse = flightOpsServiceClient.getFlight(flightInstanceResponse.getFlightId());
        if(flightResponse == null || flightResponse.getId()==null) {
            throw new Exception("Flight not found with id: " + flightInstanceResponse.getFlightId());
        }

        Fare fare = FareMapper.toFare(request);

        fare.setAirlineId(airlineResponse.getId());
        fare.setFlightId(flightResponse.getId());
        fare.setFlightInstanceId(flightInstanceResponse.getId());
        fare = fareRepository.save(fare);

        return toResponse(fare, airlineResponse, flightResponse, flightInstanceResponse);
    }

    @Override
    public FareResponse getFareById(Long id) throws Exception {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare Not Found with id"));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(fare.getAirlineId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(fare.getFlightId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(fare.getFlightInstanceId());

        return toResponse(fare, airlineResponse, flightResponse, flightInstanceResponse);
    }

    @Override
    public Page<FareResponse> searchFares(
            Long airlineId, Long flightId,
            Long flightInstanceId, CabinClass cabinClass,
            FareType fareType, FareStatus status,
            Pageable pageable
    ) {
        Page<Fare> fares = fareRepository.searchFare(
                airlineId, flightId, flightInstanceId, cabinClass, fareType, status, pageable);
        List<FareResponse> responseList = convertListToFareResponse(fares.getContent());
        return new PageImpl<>(
                responseList,
                fares.getPageable(),
                fares.getTotalElements()
        );
    }

    @Override
    public FareResponse updateFare(Long id, FareRequest request) throws Exception {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare Not Found with id"));
        FareMapper.updateFare(fare, request);
        fare = fareRepository.save(fare);

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(fare.getAirlineId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(fare.getFlightId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(fare.getFlightInstanceId());

        return toResponse(fare, airlineResponse, flightResponse, flightInstanceResponse);
    }

    @Override
    public void deleteFare(Long airlineId, Long id) throws Exception {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare Not Found with id"));
        if(!fare.getAirlineId().equals(airlineId)) {
            throw new Exception("Airline Id and Fare Id do not match");
        }
        fareRepository.delete(fare);
    }

    @Override
    public FareResponse getLowestFareByFlightId(Long flightId) throws Exception {
        Fare fare = fareRepository.getLowestFareByFlightId(flightId)
                .orElseThrow(() -> new Exception("Fare Not Found with id"));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(fare.getAirlineId());
        FlightResponse flightResponse = flightOpsServiceClient.getFlight(fare.getFlightId());
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(fare.getFlightInstanceId());

        return toResponse(fare,airlineResponse, flightResponse, flightInstanceResponse);
    }

    @Override
    public List<FareResponse> getFaresByFlightId(Long flightId) throws Exception {
        List<Fare> fares = fareRepository.findByFlightId(flightId);
        return convertListToFareResponse(fares);
    }

    private FareResponse toResponse(
            Fare fare,
            AirlineResponse airlineResponse,
            FlightResponse flightResponse,
            FlightInstanceResponse flightInstanceResponse
    ) {
        return FareMapper.toFareResponse(
                fare,
                airlineResponse,
                flightResponse,
                flightInstanceResponse
        );
    }

    private List<FareResponse> convertListToFareResponse(
            List<Fare> fares
    ) {

        Map<Long, AirlineResponse> airlineCache = new HashMap<>();
        Map<Long, FlightResponse> flightCache = new HashMap<>();
        Map<Long, FlightInstanceResponse> flightInstanceCache = new HashMap<>();

        return fares.stream()
                .map(fare -> {

                    AirlineResponse airline = airlineCache.computeIfAbsent(
                            fare.getAirlineId(),
                            id -> {
                                try {
                                    return airlineServiceClient.getAirlineById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch airline: " + id, e);
                                }
                            }
                    );

                    FlightResponse flight = flightCache.computeIfAbsent(
                            fare.getFlightId(),
                            id -> {
                                try {
                                    return flightOpsServiceClient.getFlight(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch flight: " + id, e);
                                }
                            }
                    );

                    FlightInstanceResponse flightInstance = flightInstanceCache.computeIfAbsent(
                            fare.getFlightInstanceId(),
                            id -> {
                                try {
                                    return flightOpsServiceClient.getFlightInstance(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch flightInstance: " + id, e);
                                }
                            }
                    );

                    return toResponse(
                            fare,
                            airline,
                            flight,
                            flightInstance
                    );
                })
                .toList();
    }

}
