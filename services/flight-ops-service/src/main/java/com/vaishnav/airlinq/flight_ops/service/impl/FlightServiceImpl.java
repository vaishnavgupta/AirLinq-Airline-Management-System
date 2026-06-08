package com.vaishnav.airlinq.flight_ops.service.impl;

import com.vaishnav.airlinq.flight_ops.client.AirlineServiceClient;
import com.vaishnav.airlinq.flight_ops.client.LocationServiceClient;
import com.vaishnav.airlinq.flight_ops.mapper.FlightMapper;
import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.airlinq.flight_ops.repository.FlightRepository;
import com.vaishnav.airlinq.flight_ops.service.FlightService;
import com.vaishnav.enums.FlightStatus;
import com.vaishnav.payload.request.FlightRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.FlightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final AirlineServiceClient airlineServiceClient;
    private final LocationServiceClient locationServiceClient;

    @Override
    public FlightResponse createFlight(Long ownerId, FlightRequest flightRequest) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(ownerId);
        if(airlineResponse == null || airlineResponse.getId() == null) {
            throw new Exception("Airline not found with owner id " + ownerId);
        }

        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(flightRequest.getAircraftId());
        if(aircraftResponse == null || aircraftResponse.getId() == null) {
            throw new Exception("Aircraft not found with id " + flightRequest.getAircraftId());
        }

        AirportResponse departureAirport = locationServiceClient.getAirportById(flightRequest.getDepartureAirportId());
        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flightRequest.getArrivalAirportId());

        if(departureAirport == null || arrivalAirport == null) {
            throw new Exception("Departure airport or arrival airport not found");
        }

        if(Objects.equals(departureAirport.getId(), arrivalAirport.getId())) {
            throw new Exception("Departure airport and arrival airport cannot be the same");
        }

        if(flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new Exception("Flight already exists with the flight number");
        }
        Flight flight = FlightMapper.toFlight(flightRequest);
        flight.setAirlineId(airlineResponse.getId());
        flight = flightRepository.save(flight);
        return convertToFlightResponse(
                flight, airlineResponse, aircraftResponse, departureAirport, arrivalAirport
        );
    }

    @Override
    public Page<FlightResponse> getFlightsByAirlineId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        Page<Flight> flightPage = flightRepository.getFlightsByAirportIds(
                airlineId, arrivalAirportId, departureAirportId, pageable
        );
        return convertPageToResponse(flightPage);
    }

    @Override
    public FlightResponse getFlightById(Long id) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight does not exists with id"));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(flight.getAirlineId());
        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(flight.getAircraftId());

        AirportResponse departureAirport = locationServiceClient.getAirportById(flight.getDepartureAirportId());
        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flight.getArrivalAirportId());

        return convertToFlightResponse(
                flight,
                airlineResponse,
                aircraftResponse,
                departureAirport,
                arrivalAirport
        );
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight does not exists with id"));

        if(
                flightRequest.getFlightNumber() != null
                && !flight.getFlightNumber().equals(flightRequest.getFlightNumber())
                && flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())
        ) {
            throw new Exception("Flight already exists with the flight number");
        }

        FlightMapper.updateEntity(flightRequest, flight);

        flight = flightRepository.save(flight);

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(flight.getAirlineId());
        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(flight.getAircraftId());

        AirportResponse departureAirport = locationServiceClient.getAirportById(flight.getDepartureAirportId());
        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flight.getArrivalAirportId());

        return convertToFlightResponse(
                flight,
                airlineResponse,
                aircraftResponse,
                departureAirport,
                arrivalAirport
        );
    }

    @Override
    public FlightResponse changeStatus(Long id, FlightStatus flightStatus) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight does not exists with id"));
        flight.setFlightStatus(flightStatus);
        flight = flightRepository.save(flight);

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(flight.getAirlineId());
        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(flight.getAircraftId());

        AirportResponse departureAirport = locationServiceClient.getAirportById(flight.getDepartureAirportId());
        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flight.getArrivalAirportId());

        return convertToFlightResponse(
                flight,
                airlineResponse,
                aircraftResponse,
                departureAirport,
                arrivalAirport
        );
    }

    @Override
    public void deleteFlight(Long ownerId, Long id) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(ownerId);

        if (airlineResponse == null || airlineResponse.getId() == null) {
            throw new Exception("Airline does not exists with ownerId");
        }

        Flight flight = flightRepository.findByAirlineIdAndId(airlineResponse.getId(), id)
                .orElseThrow(() -> new Exception("Flight does not exists with id & airline id"));
        flightRepository.delete(flight);
    }

    private FlightResponse convertToFlightResponse(
            Flight flight,
            AirlineResponse airlineResponse,
            AircraftResponse aircraftResponse,
            AirportResponse departureAirportResponse,
            AirportResponse arrivalAirportResponse
    ) {
        return FlightMapper.toResponse(
                flight,
                airlineResponse,
                aircraftResponse,
                arrivalAirportResponse,
                departureAirportResponse
        );
    }

    private Page<FlightResponse> convertPageToResponse(Page<Flight> page) {
        Map<Long, AirlineResponse> airlineCache = new HashMap<>();
        Map<Long, AircraftResponse> aircraftCache = new HashMap<>();
        Map<Long, AirportResponse> airportCache = new HashMap<>();

        return page.map(flight -> {
            AirlineResponse airline = airlineCache.computeIfAbsent(
                    flight.getAirlineId(),
                    id -> {
                        try {
                            return airlineServiceClient.getAirlineById(id);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to fetch airline: " + id, e);
                        }
                    }
            );
            AircraftResponse aircraft = aircraftCache.computeIfAbsent(
                    flight.getAircraftId(),
                    id -> {
                        try {
                            return airlineServiceClient.getAircraftById(id);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to fetch aircraft: " + id, e);
                        }
                    }
            );

            AirportResponse departure = airportCache.computeIfAbsent(
                    flight.getDepartureAirportId(),
                    locationServiceClient::getAirportById
            );

            AirportResponse arrival = airportCache.computeIfAbsent(
                    flight.getArrivalAirportId(),
                    locationServiceClient::getAirportById
            );

            return convertToFlightResponse(
                    flight,
                    airline,
                    aircraft,
                    departure,
                    arrival
            );
        });
    }
}
