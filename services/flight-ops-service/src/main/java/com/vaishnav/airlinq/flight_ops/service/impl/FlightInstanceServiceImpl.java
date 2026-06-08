package com.vaishnav.airlinq.flight_ops.service.impl;

import com.vaishnav.airlinq.flight_ops.client.AirlineServiceClient;
import com.vaishnav.airlinq.flight_ops.client.LocationServiceClient;
import com.vaishnav.airlinq.flight_ops.mapper.FlightInstanceMapper;
import com.vaishnav.airlinq.flight_ops.mapper.FlightMapper;
import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.airlinq.flight_ops.model.FlightInstance;
import com.vaishnav.airlinq.flight_ops.repository.FlightInstanceRepository;
import com.vaishnav.airlinq.flight_ops.repository.FlightRepository;
import com.vaishnav.airlinq.flight_ops.service.FlightInstanceService;
import com.vaishnav.payload.request.FlightInstanceRequest;
import com.vaishnav.payload.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FlightInstanceServiceImpl implements FlightInstanceService {
    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;
    private final AirlineServiceClient airlineServiceClient;
    private final LocationServiceClient locationServiceClient;

    @Override
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest request) throws Exception {
        AirlineResponse airline = airlineServiceClient.getAirlineByUserId(userId);
        if(airline == null || airline.getId() == null) {
            throw new Exception("Airline not found with owner id " + userId);
        }
        request.setAirlineId(airline.getId());

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new Exception("Flight not found with id: " + request.getFlightId()));

        AircraftResponse aircraft = airlineServiceClient.getAircraftById(flight.getAircraftId());

        if(aircraft == null || aircraft.getId() == null) {
            throw new Exception("Aircraft not found with id " );
        }

        FlightInstance flightInstance = FlightInstanceMapper.toFlightInstance(request, flight);

        flightInstance.setTotalSeats(aircraft.getTotalSeats());

        // If availableSeats not provided, default to totalSeats (all seats free on creation)
        if (flightInstance.getAvailableSeats() == null) {
            flightInstance.setAvailableSeats(aircraft.getTotalSeats());
        }

        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flight.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flight.getDepartureAirportId());

        flightInstance = flightInstanceRepository.save(flightInstance);

        // TODO: publish event or call Seat Service to create seat instances

        return buildResponse(flightInstance, airline, aircraft, arrivalAirport, departureAirport);
    }

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) throws Exception {
        FlightInstance flightInstance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id: " + id));

        AirlineResponse airline = airlineServiceClient.getAirlineById(flightInstance.getAirlineId());

        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flightInstance.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flightInstance.getDepartureAirportId());

        AircraftResponse aircraft = airlineServiceClient.getAircraftById(flightInstance.getFlight().getAircraftId());

        return buildResponse(flightInstance, airline, aircraft, arrivalAirport, departureAirport);
    }

    @Override
    public Page<FlightInstanceResponse> getByAirlineId(
            Long airlineId,
            Long departureAirportId,
            Long arrivalAirportId,
            Long flightId,
            LocalDateTime onDate,
            Pageable pageable
    ) {
        Page<FlightInstance> page = flightInstanceRepository
                .search(airlineId, departureAirportId, arrivalAirportId, flightId, onDate, pageable);

        return convertPageToFlightInstanceResponse(page);
    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest request) throws Exception {
        FlightInstance flightInstance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id: " + id));

        FlightInstanceMapper.updateEntity(flightInstance, request);
        flightInstance = flightInstanceRepository.save(flightInstance);

        AirlineResponse airline = airlineServiceClient.getAirlineById(flightInstance.getAirlineId());

        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flightInstance.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flightInstance.getDepartureAirportId());

        AircraftResponse aircraft = airlineServiceClient.getAircraftById(flightInstance.getFlight().getAircraftId());

        return buildResponse(flightInstance, airline, aircraft, arrivalAirport, departureAirport);

    }

    @Override
    public void deleteFlightInstance(Long id) throws Exception {
        FlightInstance flightInstance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id: " + id));

        flightInstance.setIsActive(false);
        flightInstanceRepository.delete(flightInstance);
    }


    private FlightInstanceResponse buildResponse(
            FlightInstance fi,
            AirlineResponse airline,
            AircraftResponse aircraft,
            AirportResponse arrival,
            AirportResponse departure
    ) {

        return FlightInstanceMapper.toResponse(
                fi,
                aircraft,
                airline,
                departure,
                arrival
        );
    }

    private Page<FlightInstanceResponse> convertPageToFlightInstanceResponse(
            Page<FlightInstance> page
    ) {

        Map<Long, AirlineResponse> airlineCache = new HashMap<>();
        Map<Long, AircraftResponse> aircraftCache = new HashMap<>();
        Map<Long, AirportResponse> airportCache = new HashMap<>();

        return page.map(fi -> {

            AirlineResponse airline = airlineCache.computeIfAbsent(
                    fi.getAirlineId(),
                    id -> {
                        try {
                            return airlineServiceClient.getAirlineById(id);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to fetch airline: " + id, e);
                        }
                    }
            );

            Long aircraftId = fi.getFlight() != null
                    ? fi.getFlight().getAircraftId()
                    : null;

            AircraftResponse aircraft = null;
            if (aircraftId != null) {
                aircraft = aircraftCache.computeIfAbsent(
                        aircraftId,
                        id -> {
                            try {
                                return airlineServiceClient.getAircraftById(id);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to fetch aircraft: " + id, e);
                            }
                        }
                );
            }

            AirportResponse departure = airportCache.computeIfAbsent(
                    fi.getDepartureAirportId(),
                    locationServiceClient::getAirportById
            );

            AirportResponse arrival = airportCache.computeIfAbsent(
                    fi.getArrivalAirportId(),
                    locationServiceClient::getAirportById
            );

            return buildResponse(
                    fi,
                    airline,
                    aircraft,
                    arrival,
                    departure
            );
        });
    }

}
