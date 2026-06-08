package com.vaishnav.airlinq.flight_ops.mapper;

import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.payload.request.FlightRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.FlightResponse;

import java.time.LocalDateTime;

public class FlightMapper {

    public static Flight toFlight(FlightRequest flightRequest) {
        if(flightRequest == null) return null;

        return Flight.builder()
                .flightNumber(flightRequest.getFlightNumber())
                .airlineId(flightRequest.getAirlineId())
                .aircraftId(flightRequest.getAircraftId())
                .departureAirportId(flightRequest.getDepartureAirportId())
                .arrivalAirportId(flightRequest.getArrivalAirportId())
                .flightStatus(flightRequest.getFlightStatus())
                .build();
    }

    public static FlightResponse toResponse(
            Flight flight,
            AirlineResponse airlineResponse,
            AircraftResponse aircraftResponse,
            AirportResponse arrivalAirportResponse,
            AirportResponse departureAirportResponse
    ) {
        if(flight == null) return null;
        return FlightResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airline(airlineResponse)
                .aircraft(aircraftResponse)
                .departureAirport(departureAirportResponse)
                .arrivalAirport(arrivalAirportResponse)
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now())
                .flightStatus(flight.getFlightStatus())
                .lowestPrice(767.33)
                .totalAvailableSeats(22)
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

    public static void updateEntity(FlightRequest flightRequest, Flight flight) {
        if(flightRequest == null || flight == null) return;

        if(flightRequest.getFlightNumber() != null) flight.setFlightNumber(flightRequest.getFlightNumber());
        if(flightRequest.getAircraftId() != null) flight.setAircraftId(flightRequest.getAircraftId());
        if(flightRequest.getDepartureAirportId() != null) flight.setDepartureAirportId(flightRequest.getDepartureAirportId());
        if(flightRequest.getArrivalAirportId() != null) flight.setArrivalAirportId(flightRequest.getArrivalAirportId());
        if(flightRequest.getFlightStatus() != null) flight.setFlightStatus(flightRequest.getFlightStatus());
    }

}
