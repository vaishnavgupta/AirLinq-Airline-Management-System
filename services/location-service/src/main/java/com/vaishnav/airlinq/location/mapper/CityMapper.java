package com.vaishnav.airlinq.location.mapper;

import com.vaishnav.airlinq.location.model.City;
import com.vaishnav.payload.request.CityRequest;
import com.vaishnav.payload.response.CityResponse;

public class CityMapper {

    public static City getCity(CityRequest request) {
        if(request == null ) return null;
        return City.builder()
                .name(request.getName())
                .cityCode(request.getCityCode())
                .countryCode(request.getCountryCode())
                .countryName(request.getCountryName())
                .regionCode(request.getRegionCode())
                .timeZoneId(request.getTimeZoneId())
                .build();
    }

    public static CityResponse getCityResponse(City city) {
        if(city == null) return null;
        return CityResponse.builder()
                .name(city.getName())
                .id(city.getId())
                .cityCode(city.getCityCode())
                .countryCode(city.getCountryCode())
                .countryName(city.getCountryName())
                .timeZoneId(city.getTimeZoneId())
                .regionCode(city.getRegionCode())
                .build();
    }

    public static City updateCity(City city, CityRequest request) {
        if(request.getName() != null) {
            city.setName(request.getName());
        }
        if(request.getCityCode() != null) {
            city.setCityCode(request.getCityCode());
        }
        if(request.getCountryCode() != null) {
            city.setCountryCode(request.getCountryCode());
        }
        if(request.getCountryName() != null) {
            city.setCountryName(request.getCountryName());
        }
        if(request.getRegionCode() != null) {
            city.setRegionCode(request.getRegionCode());
        }
        return city;
    }

}
