package com.vaishnav.airlinq.location.service.impl;

import com.vaishnav.airlinq.location.mapper.AirportMapper;
import com.vaishnav.airlinq.location.model.Airport;
import com.vaishnav.airlinq.location.repository.AirportRepository;
import com.vaishnav.airlinq.location.repository.CityRepository;
import com.vaishnav.airlinq.location.service.AirportService;
import com.vaishnav.payload.request.AirportRequest;
import com.vaishnav.payload.response.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest request) {
        if(airportRepository.existsByIataCode(request.getIataCode()))
            throw new RuntimeException("Airport already exists with IATA code");
        Airport airport = AirportMapper.getAirport(request);
        airport.setCity(cityRepository.findById(request.getCityId())
                .orElseThrow(()  -> new RuntimeException("City not found")));
        airport = airportRepository.save(airport);
        return AirportMapper.getAirportResponse(airport);
    }

    @Override
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id " + id));

        return AirportMapper.getAirportResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        return airports.stream()
                .map(AirportMapper::getAirportResponse)
                .toList();
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id " + id));

        if(request.getIataCode() != null
                && !airport.getIataCode().equals(request.getIataCode())
                && airportRepository.existsByIataCode(request.getIataCode())) {
            throw new RuntimeException("Airport already exists with IATA code");
        }
        if(request.getCityId() != null && !cityRepository.existsById(request.getCityId())) {
            throw new RuntimeException("City not found with id " + request.getCityId());
        }
        if(request.getCityId() != null) {
            airport.setCity(cityRepository.findById(request.getCityId())
                    .orElseThrow(()  -> new RuntimeException("City not found")));
        }
        airport = AirportMapper.updateAirport(request, airport);

        airport = airportRepository.save(airport);
        return AirportMapper.getAirportResponse(airport);
    }

    @Override
    public void deleteAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id " + id));
        airportRepository.delete(airport);
    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {
        if(!cityRepository.existsById(cityId)) {
            throw new RuntimeException("City not found with id " + cityId);
        }
        return airportRepository.findByCityId(cityId)
                .stream()
                .map(AirportMapper::getAirportResponse)
                .toList();
    }
}
