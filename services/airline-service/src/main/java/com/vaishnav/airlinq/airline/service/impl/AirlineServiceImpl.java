package com.vaishnav.airlinq.airline.service.impl;

import com.vaishnav.airlinq.airline.client.UserServiceClient;
import com.vaishnav.airlinq.airline.mapper.AirlineMapper;
import com.vaishnav.airlinq.airline.model.Airline;
import com.vaishnav.airlinq.airline.repository.AirlineRepository;
import com.vaishnav.airlinq.airline.service.AirlineService;
import com.vaishnav.enums.AirlineStatus;
import com.vaishnav.enums.UserRole;
import com.vaishnav.payload.dto.UserDto;
import com.vaishnav.payload.request.AirlineRequest;
import com.vaishnav.payload.response.AirlineDropDownItem;
import com.vaishnav.payload.response.AirlineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public AirlineResponse createAirline(AirlineRequest request, Long ownerId) throws Exception {
        UserDto userDto = userServiceClient.getUserById(ownerId);
        if(userDto == null || userDto.getId()==null){
            throw new Exception("User does not exists with ownerId");
        }
        if(!userDto.getRole().equals(UserRole.ROLE_AIRLINE_OWNER)){
            throw new Exception("User does not have privileges to create airline");
        }

        Airline airline = AirlineMapper.toAirline(request, userDto.getId());
        airline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineByOwner(Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with provided owner id"));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineById(Long id) throws Exception {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new Exception("Airline does not exists with provided  id"));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable)
                .map(AirlineMapper::toResponse);
    }

    @Override
    public AirlineResponse updateAirline(AirlineRequest request, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with provided owner id"));

        AirlineMapper.updateEntity(airline, request);
        airline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public void deleteAirline(Long id, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline does not exists with provided owner id"));
        airlineRepository.delete(airline);
    }

    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) throws Exception {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new Exception("Airline does not exists with provided  id"));

        airline.setStatus(status);
        airline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public List<AirlineDropDownItem> getAirlineDropdown() {
        return airlineRepository.findByStatus(AirlineStatus.ACTIVE)
                .stream()
                .map(AirlineMapper::toDropDownItem)
                .toList();
    }
}
