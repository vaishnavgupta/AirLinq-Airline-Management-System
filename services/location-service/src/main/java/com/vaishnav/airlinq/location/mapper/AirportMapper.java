package com.vaishnav.airlinq.location.mapper;

import com.vaishnav.airlinq.location.model.Airport;
import com.vaishnav.payload.request.AirportRequest;
import com.vaishnav.payload.response.AirportResponse;

import java.time.ZoneId;

public class AirportMapper {

    public static Airport getAirport(AirportRequest request) {
        if(request == null)
            return null;
        return Airport.builder()
                .iataCode(request.getIataCode())
                .name(request.getName())
                .address(request.getAddress())
                .geoCode(request.getGeoCode())
                .timeZoneId(request.getTimeZone().getId())
                .build();
        // City to be added in Service
    }

    public static AirportResponse getAirportResponse(Airport airport) {
        if(airport == null)
            return null;
        return AirportResponse.builder()
                .id(airport.getId())
                .iataCode(airport.getIataCode())
                .name(airport.getName())
                .detailedName(airport.getDetailedName())
                .timeZone(ZoneId.of(airport.getTimeZoneId()))
                .address(airport.getAddress())
                .cityResponse(CityMapper.getCityResponse(airport.getCity()))
                .geoCode(airport.getGeoCode())
                .build();
    }

    public static Airport updateAirport(AirportRequest airportRequest, Airport airport) {
        if(airportRequest == null || airport == null)
            return null;

        if(airportRequest.getName() != null) {
            airport.setName(airportRequest.getName());
        }
        if(airportRequest.getTimeZone() != null) {
            airport.setTimeZoneId(airportRequest.getTimeZone().getId());
        }
        if(airportRequest.getAddress() != null) {
            airport.setAddress(airportRequest.getAddress());
        }
        if(airportRequest.getGeoCode() != null) {
            airport.setGeoCode(airportRequest.getGeoCode());
        }
        // City to be added in Service
        return airport;
    }

}
