package com.vaishnav.airlinq.flight_ops.mapper;

import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.airlinq.flight_ops.model.FlightInstance;
import com.vaishnav.enums.FlightStatus;
import com.vaishnav.payload.request.FlightInstanceRequest;
import com.vaishnav.payload.request.FlightRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;

public class FlightInstanceMapper {

    public static FlightInstance toFlightInstance(FlightInstanceRequest request, Flight flight) {
        if (request == null || flight == null) return null;

        return FlightInstance.builder()
                .airlineId(request.getAirlineId())
                .flight(flight)
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .scheduleId(request.getScheduleId())
                .departureTime(request.getDepartureDateTime())
                .arrivalTime(request.getArrivalDateTime())
                .availableSeats(request.getAvailableSeats() != null
                        ? request.getAvailableSeats()
                        : request.getTotalSeats())
                .status(request.getStatus() != null
                        ? request.getStatus()
                        : FlightStatus.SCHEDULED)
                .minAdvanceBookingDays(request.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(request.getMaxAdvanceBookingDays())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .gate(request.getGate() != null ? request.getGate() : null)
                .terminal(request.getTerminal() != null ? request.getTerminal() : null)
                .build();
    }

    public static FlightInstanceResponse toResponse(FlightInstance fi,
                                                    AircraftResponse aircraftResponse,
                                                    AirlineResponse airline,
                                                    AirportResponse departureAirport,
                                                    AirportResponse arrivalAirport) {
        if (fi == null) return null;

        return FlightInstanceResponse.builder()
                .id(fi.getId())
                .flightId(fi.getFlight() != null ? fi.getFlight().getId() : null)
                .flightNumber(fi.getFlight() != null ? fi.getFlight().getFlightNumber() : null)
                .aircraftId(fi.getFlight() != null ? fi.getFlight().getAircraftId() : null)
                .aircraftModal(aircraftResponse != null ? aircraftResponse.getModel() : null)
                .aircraftCode(aircraftResponse != null ? aircraftResponse.getCode() : null)
                .airlineId(fi.getAirlineId())
                .airlineName(airline != null ? airline.getName() : null)
                .airlineLogo(airline != null ? airline.getLogoUrl() : null)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureDateTime(fi.getDepartureTime())
                .arrivalDateTime(fi.getArrivalTime())
                .formattedDuration(fi.getFormattedDuration())
                .totalSeats(fi.getTotalSeats())
                .availableSeats(fi.getAvailableSeats())
                .status(fi.getStatus())
                .minAdvanceBookingDays(fi.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(fi.getMaxAdvanceBookingDays())
                .isActive(fi.getIsActive())
                .terminal(fi.getTerminal())
                .gate(fi.getGate())
                .build();
    }

    public static void updateEntity(FlightInstance flightInstance, FlightInstanceRequest request) {
        if (flightInstance == null || request == null) return;

        if (request.getAirlineId() != null)             flightInstance.setAirlineId(request.getAirlineId());
        if (request.getDepartureAirportId() != null)    flightInstance.setDepartureAirportId(request.getDepartureAirportId());
        if (request.getArrivalAirportId() != null)      flightInstance.setArrivalAirportId(request.getArrivalAirportId());
        if (request.getScheduleId() != null)            flightInstance.setScheduleId(request.getScheduleId());
        if (request.getDepartureDateTime() != null)     flightInstance.setDepartureTime(request.getDepartureDateTime());
        if (request.getArrivalDateTime() != null)       flightInstance.setArrivalTime(request.getArrivalDateTime());
        if (request.getTotalSeats() != null)            flightInstance.setTotalSeats(request.getTotalSeats());
        if (request.getAvailableSeats() != null)        flightInstance.setAvailableSeats(request.getAvailableSeats());
        if (request.getStatus() != null)                flightInstance.setStatus(request.getStatus());
        if (request.getMinAdvanceBookingDays() != null) flightInstance.setMinAdvanceBookingDays(request.getMinAdvanceBookingDays());
        if (request.getMaxAdvanceBookingDays() != null) flightInstance.setMaxAdvanceBookingDays(request.getMaxAdvanceBookingDays());
        if (request.getIsActive() != null)              flightInstance.setIsActive(request.getIsActive());
        if(request.getGate() != null)                   flightInstance.setGate(request.getGate());
        if(request.getTerminal() != null)               flightInstance.setGate(request.getTerminal());
    }
}
