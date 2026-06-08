package com.vaishnav.airlinq.airline.service.impl;

import com.vaishnav.airlinq.airline.mapper.AircraftMapper;
import com.vaishnav.airlinq.airline.model.Aircraft;
import com.vaishnav.airlinq.airline.model.Airline;
import com.vaishnav.airlinq.airline.repository.AircraftRepository;
import com.vaishnav.airlinq.airline.repository.AirlineRepository;
import com.vaishnav.airlinq.airline.service.AircraftService;
import com.vaishnav.payload.request.AircraftRequest;
import com.vaishnav.payload.response.AircraftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Override
    public AircraftResponse createAircraft(AircraftRequest request, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with owner id"));
        Aircraft aircraft = AircraftMapper.toAircraft(request, airline);
        if(aircraftRepository.existsByCode(aircraft.getCode())) {
            throw new Exception("Aircraft already exists with code " + aircraft.getCode());
        }
        if(aircraft.getSeatingCapacity() > aircraft.getTotalSeats()) {
            throw new Exception("Seating capacity is more than aircraft capacity");
        }
        aircraft =  aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(aircraft);
    }

    @Override
    public AircraftResponse getById(Long id) throws Exception {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new Exception("Airline does not exists with id"));
        return AircraftMapper.toResponse(aircraft);
    }

    @Override
    public List<AircraftResponse> listAllAircraftByOwner(Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with owner id"));
        return aircraftRepository.findByAirline(airline)
                .stream()
                .map(AircraftMapper::toResponse).toList();
    }

    @Override
    public AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with owner id"));

        Aircraft aircraft = aircraftRepository.findByIdAndAirline(id, airline)
                .orElseThrow(() -> new Exception("Aircraft does not exists with id and airline"));

        AircraftMapper.updateEntity(aircraft, request);

        if(request.getCode() != null &&
                !aircraft.getCode().equals(request.getCode()) &&
                aircraftRepository.existsByCode(aircraft.getCode())
        ) {
            throw new Exception("Aircraft already exists with code " + aircraft.getCode());
        }

        if(aircraft.getSeatingCapacity() > aircraft.getTotalSeats()) {
            throw new Exception("Seating capacity is more than aircraft capacity");
        }

        aircraft =  aircraftRepository.save(aircraft);

        return AircraftMapper.toResponse(aircraft);
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with owner id"));

        Aircraft aircraft = aircraftRepository.findByIdAndAirline(id, airline)
                .orElseThrow(() -> new Exception("Aircraft does not exists with id and airline"));

        aircraftRepository.delete(aircraft);
    }
}
