package com.vaishnav.airlinq.airline.mapper;

import com.vaishnav.airlinq.airline.model.Airline;
import com.vaishnav.embeddable.Support;
import com.vaishnav.payload.request.AirlineRequest;
import com.vaishnav.payload.response.AirlineDropDownItem;
import com.vaishnav.payload.response.AirlineResponse;

public class AirlineMapper {

    public static Airline toAirline(AirlineRequest request, Long ownerId) {

        if(request == null)
            return null;

        Airline airline = Airline.builder()
                .iataCode(request.getIataCode())
                .icaoCode(request.getIcaoCode())
                .name(request.getName())
                .alias(request.getAlias())
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .ownerId(ownerId)
                .status(request.getStatus())
                .alliance(request.getAlliance())
                .headquarterCityId(request.getHeadquartersCityId())
                .build();

        Support support = new Support();
        if(request.getSupportEmail() != null) support.setEmail(request.getSupportEmail());
        if(request.getSupportHours() != null) support.setHours(request.getSupportHours());
        if(request.getSupportPhone() != null) support.setPhone(request.getSupportPhone());
        airline.setSupport(support);

        return airline;
    }

    public static AirlineResponse toResponse(Airline airline) {
        if(airline == null) return null;

        return AirlineResponse.builder()
                .id(airline.getId())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .name(airline.getName())
                .alias(airline.getAlias())
                .logoUrl(airline.getLogoUrl())
                .website(airline.getWebsite())
                .status(airline.getStatus())
                .alliance(airline.getAlliance())
                .createdAt(airline.getCreatedAt())
                .updatedAt(airline.getUpdatedAt())
                .ownerId(airline.getOwnerId())
                .support(airline.getSupport())
                .build();
    }

    public static void updateEntity(Airline airline, AirlineRequest request) {
        if (airline == null || request == null) return;

        if (request.getIataCode() != null)        airline.setIataCode(request.getIataCode());
        if (request.getIcaoCode() != null)        airline.setIcaoCode(request.getIcaoCode());
        if (request.getName() != null)            airline.setName(request.getName());
        if (request.getAlias() != null)           airline.setAlias(request.getAlias());
        if (request.getLogoUrl() != null)         airline.setLogoUrl(request.getLogoUrl());
        if (request.getWebsite() != null)         airline.setWebsite(request.getWebsite());
        if (request.getStatus() != null)          airline.setStatus(request.getStatus());
        if (request.getAlliance() != null)         airline.setAlliance(request.getAlliance());
        if (request.getHeadquartersCityId() != null) airline.setHeadquarterCityId(request.getHeadquartersCityId());

        if (request.getSupportEmail() != null || request.getSupportPhone() != null || request.getSupportHours() != null) {
            if (airline.getSupport() == null) {
                airline.setSupport(new Support());
            }
            if (request.getSupportEmail() != null) airline.getSupport().setEmail(request.getSupportEmail());
            if (request.getSupportPhone() != null) airline.getSupport().setPhone(request.getSupportPhone());
            if (request.getSupportHours() != null) airline.getSupport().setHours(request.getSupportHours());
        }
    }

    public static AirlineDropDownItem toDropDownItem(Airline airline) {
        if(airline == null) return null;

        return AirlineDropDownItem.builder()
                .id(airline.getId())
                .name(airline.getName())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .logoUrl(airline.getLogoUrl())
                .build();
    }


}
