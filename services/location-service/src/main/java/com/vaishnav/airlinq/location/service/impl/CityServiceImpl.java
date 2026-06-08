package com.vaishnav.airlinq.location.service.impl;

import com.vaishnav.airlinq.location.mapper.CityMapper;
import com.vaishnav.airlinq.location.model.City;
import com.vaishnav.payload.request.CityRequest;
import com.vaishnav.payload.response.CityResponse;
import com.vaishnav.airlinq.location.repository.CityRepository;
import com.vaishnav.airlinq.location.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public CityResponse createCity(CityRequest request) {
        if(cityRepository.existsByCityCode(request.getCityCode())) {
            throw new RuntimeException("City with code already exists");
        }
        City city = CityMapper.getCity(request);
        city = cityRepository.save(city);
        return CityMapper.getCityResponse(city);
    }

    @Override
    public CityResponse getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City with id " + id + " not found"));
        return CityMapper.getCityResponse(city);
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City with id " + id + " not found"));

        if(!city.getCityCode().equals(request.getCityCode()) && cityRepository.existsByCityCode(request.getCityCode())) {
            throw new RuntimeException("City with code already exists");
        }

        CityMapper.updateCity(city, request);

        city = cityRepository.save(city);

        return CityMapper.getCityResponse(city);
    }

    @Override
    public void deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City with id " + id + " not found"));
        cityRepository.delete(city);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable)
                .map(CityMapper::getCityResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        return cityRepository.searchByKeyword(keyword, pageable)
                .map(CityMapper::getCityResponse);
    }

    @Override
    public Page<CityResponse> searchCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable)
                .map(CityMapper::getCityResponse);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }

}
