package com.vaishnav.airlinq.airline.mapper;

import com.vaishnav.airlinq.airline.model.Aircraft;
import com.vaishnav.airlinq.airline.model.Airline;
import com.vaishnav.payload.request.AircraftRequest;
import com.vaishnav.payload.response.AircraftResponse;

public class AircraftMapper {

    public static Aircraft toAircraft(AircraftRequest request, Airline airline) {
        if (request == null || airline == null) return null;
        return Aircraft.builder()
                .code(request.getCode())
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .seatingCapacity(request.getSeatingCapacity())
                .economySeats(request.getEconomySeats())
                .premiumEconomySeats(request.getPremiumEconomySeats())
                .businessSeats(request.getBusinessSeats())
                .firstClassSeats(request.getFirstClassSeats())
                .cruisingSpeedKmh(request.getCruisingSpeedKmh())
                .rangeKmh(request.getRangeKmh())
                .yearOfManufacturer(request.getYearOfManufacturer())
                .registrationDate(request.getRegistrationDate())
                .nextMaintenanceDate(request.getNextMaintenanceDate())
                .status(request.getStatus())
                .isAvailable(request.getIsAvailable())
                .airline(airline)
                .currentAirportId(request.getCurrentAirportId())
                .build();
    }

    public static AircraftResponse toResponse(Aircraft aircraft) {
        if (aircraft == null) return null;
        AircraftResponse response = AircraftResponse.builder()
                .id(aircraft.getId())
                .code(aircraft.getCode())
                .model(aircraft.getModel())
                .manufacturer(aircraft.getManufacturer())
                .seatingCapacity(aircraft.getSeatingCapacity())
                .economySeats(aircraft.getEconomySeats())
                .premiumEconomySeats(aircraft.getPremiumEconomySeats())
                .firstClassSeats(aircraft.getFirstClassSeats())
                .businessSeats(aircraft.getBusinessSeats())
                .rangeKm(aircraft.getRangeKmh())
                .cruisingSpeedKmh(aircraft.getCruisingSpeedKmh())
                .yearOfManufacture(aircraft.getYearOfManufacturer())
                .registrationDate(aircraft.getRegistrationDate())
                .nextMaintenanceDate(aircraft.getNextMaintenanceDate())
                .status(aircraft.getStatus())
                .isAvailable(aircraft.getIsAvailable())
                .createdAt(aircraft.getCreatedAt())
                .updatedAt(aircraft.getUpdatedAt())
                .build();

        if (aircraft.getAirline() != null) {
            response.setAirlineId(aircraft.getAirline().getId());
            response.setAirlineName(aircraft.getAirline().getName());
            response.setAirlineIataCode(aircraft.getAirline().getIataCode());
        }

        if (aircraft.getCurrentAirportId() != null) {
            response.setCurrentAirportId(aircraft.getCurrentAirportId());
            //Left Some fields related to CurrentAirportId
        }

        response.setTotalSeats(aircraft.getTotalSeats());
        response.setRequiresMaintenance(aircraft.requireMaintenance());
        response.setIsOperational(aircraft.isOperational());

        return response;
    }

    public static void updateEntity(Aircraft aircraft, AircraftRequest request) {
        if (aircraft == null || request == null) return;

        if (request.getCode() != null)                 aircraft.setCode(request.getCode());
        if (request.getModel() != null)                aircraft.setModel(request.getModel());
        if (request.getManufacturer() != null)         aircraft.setManufacturer(request.getManufacturer());
        if (request.getSeatingCapacity() != null)      aircraft.setSeatingCapacity(request.getSeatingCapacity());
        if (request.getEconomySeats() != null)         aircraft.setEconomySeats(request.getEconomySeats());
        if (request.getPremiumEconomySeats() != null)  aircraft.setPremiumEconomySeats(request.getPremiumEconomySeats());
        if (request.getBusinessSeats() != null)        aircraft.setBusinessSeats(request.getBusinessSeats());
        if (request.getFirstClassSeats() != null)      aircraft.setFirstClassSeats(request.getFirstClassSeats());
        if (request.getRangeKmh() != null)             aircraft.setRangeKmh(request.getRangeKmh());
        if (request.getCruisingSpeedKmh() != null)     aircraft.setCruisingSpeedKmh(request.getCruisingSpeedKmh());
        if (request.getYearOfManufacturer() != null)   aircraft.setYearOfManufacturer(request.getYearOfManufacturer());
        if (request.getRegistrationDate() != null)     aircraft.setRegistrationDate(request.getRegistrationDate());
        if (request.getNextMaintenanceDate() != null)  aircraft.setNextMaintenanceDate(request.getNextMaintenanceDate());
        if (request.getStatus() != null)               aircraft.setStatus(request.getStatus());
        if (request.getIsAvailable() != null)          aircraft.setIsAvailable(request.getIsAvailable());
        if (request.getCurrentAirportId() != null)     aircraft.setCurrentAirportId(request.getCurrentAirportId());
    }

}
