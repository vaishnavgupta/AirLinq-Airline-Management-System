package com.vaishnav.airlinq.flight_ops.mapper;

import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.airlinq.flight_ops.model.FlightSchedule;
import com.vaishnav.payload.request.FlightScheduleRequest;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.FlightScheduleResponse;

public class FlightScheduleMapper {

    public static FlightSchedule toFlightSchedule(FlightScheduleRequest request, Flight flight) {
        if (request == null || flight == null) {
            return null;
        }
        return FlightSchedule.builder()
                .flight(flight)
                .departureAirportId(flight.getDepartureAirportId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .operatingDays(request.getOpeningDays())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public static FlightScheduleResponse toFlightScheduleResponse(
            FlightSchedule flightSchedule,
            AirportResponse departureAirport,
            AirportResponse arrivalAirport
    ) {
        if (flightSchedule == null) {
            return null;
        }

        return FlightScheduleResponse.builder()
                .id(flightSchedule.getId())
                .flightId(flightSchedule.getFlight().getId())
                .flightNumber(flightSchedule.getFlight().getFlightNumber())
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureTime(flightSchedule.getDepartureTime())
                .arrivalTime(flightSchedule.getArrivalTime())
                .startDate(flightSchedule.getStartDate())
                .endDate(flightSchedule.getEndDate())
                .operatingDays(flightSchedule.getOperatingDays())
                .isActive(flightSchedule.getIsActive())
                .build();

    }

    public static void updateEntity(FlightScheduleRequest request, FlightSchedule existing) {
        if (request == null || existing == null) return;

        if (request.getDepartureTime() != null) {
            existing.setDepartureTime(request.getDepartureTime());
        }

        if (request.getArrivalTime() != null) {
            existing.setArrivalTime(request.getArrivalTime());
        }

        if (request.getStartDate() != null) {
            existing.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            existing.setEndDate(request.getEndDate());
        }

        if (request.getOpeningDays() != null) {
            existing.setOperatingDays(request.getOpeningDays());
        }

        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }
    }


}
