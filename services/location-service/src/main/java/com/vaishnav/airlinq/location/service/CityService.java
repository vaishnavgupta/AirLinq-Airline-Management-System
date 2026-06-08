package com.vaishnav.airlinq.location.service;

import com.vaishnav.payload.request.CityRequest;
import com.vaishnav.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

    CityResponse createCity(CityRequest request);

    CityResponse getCityById(Long id);

    CityResponse updateCity(Long id, CityRequest request);

    void deleteCity(Long id);

    Page<CityResponse> getAllCities(Pageable  pageable);

    Page<CityResponse> searchCities(String keyword, Pageable pageable);

    Page<CityResponse> searchCitiesByCountryCode(String countryCode, Pageable pageable);

    boolean cityExists(String cityCode);

}
