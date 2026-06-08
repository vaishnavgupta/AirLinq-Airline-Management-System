package com.vaishnav.airlinq.seat.service.impl;

import com.vaishnav.airlinq.seat.client.AirlineServiceClient;
import com.vaishnav.airlinq.seat.mapper.SeatMapMapper;
import com.vaishnav.airlinq.seat.model.SeatMap;
import com.vaishnav.airlinq.seat.repository.SeatMapRepository;
import com.vaishnav.airlinq.seat.service.SeatMapService;
import com.vaishnav.payload.request.SeatMapRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.SeatMapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeatMapServiceImpl implements SeatMapService {
    private final SeatMapRepository seatMapRepository;
    private final AirlineServiceClient airlineServiceClient;

    @Override
    public SeatMapResponse createSeatMap(Long userId, SeatMapRequest request) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);

        if(airlineResponse==null || airlineResponse.getId()==null) {
            throw new Exception("Airline not found with userId");
        }

        if(!request.getAirlineId().equals(airlineResponse.getId())) {
            throw new Exception("Airline Id does not match with provided id");
        }

        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(request.getAircraftId());
        if(aircraftResponse==null || aircraftResponse.getId()==null) {
            throw new Exception("Aircraft not found with provided id");
        }

        SeatMap seatMap = SeatMapMapper.toSeatMap(request);
        seatMap = seatMapRepository.save(seatMap);
        return toMapResponse(seatMap, airlineResponse, aircraftResponse);
    }


    @Override
    public SeatMapResponse getSeatMapById(Long id) throws Exception {
        SeatMap seatMap = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("SeatMap Not Found with id: " + id));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(seatMap.getAirlineId());
        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(seatMap.getAircraftId());

        if(aircraftResponse==null || airlineResponse.getId()==null) {
            throw new Exception("Aircraft not found with provided id");
        }

        return toMapResponse(seatMap, airlineResponse, aircraftResponse);
    }

    @Override
    public List<SeatMapResponse> getSeatMapsByAirlineId(Long airlineId) {
        List<SeatMap> seatMaps = seatMapRepository.findByAirlineIdAndIsActiveTrue(airlineId);
        return convertListToSeatMapResponse(seatMaps);
    }

    @Override
    public List<SeatMapResponse> getSeatMapsByAircraftId(Long aircraftId) {
        List<SeatMap> seatMaps = seatMapRepository.findByAircraftIdAndIsActiveTrue(aircraftId);
        return convertListToSeatMapResponse(seatMaps);
    }

    @Override
    public SeatMapResponse updateSeatMap(Long id, SeatMapRequest request) throws Exception {
        SeatMap seatMap = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("SeatMap Not Found with id: " + id));
        SeatMapMapper.updateSeatMap(seatMap, request);
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineById(seatMap.getAirlineId());
        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(seatMap.getAircraftId());
        return toMapResponse(seatMap, airlineResponse, aircraftResponse);
    }

    @Override
    public void deleteSeatMap(Long userId, Long id) throws Exception {
        SeatMap seatMap = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("SeatMap Not Found with id: " + id));

        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);

        if(airlineResponse==null || airlineResponse.getId()==null) {
            throw new Exception("Airline not found with userId");
        }
        if(!seatMap.getAirlineId().equals(airlineResponse.getId())) {
            throw new Exception("Airline Id does not match");
        }
        seatMapRepository.delete(seatMap);
    }

    private SeatMapResponse toMapResponse(SeatMap seatMap, AirlineResponse airlineResponse, AircraftResponse aircraftResponse) {
        return SeatMapMapper.toResponse(seatMap, airlineResponse, aircraftResponse);
    }

    private List<SeatMapResponse> convertListToSeatMapResponse(
            List<SeatMap> seatMaps
    ) {

        Map<Long, AirlineResponse> airlineCache = new HashMap<>();
        Map<Long, AircraftResponse> aircraftCache = new HashMap<>();

        return seatMaps.stream()
                .map(seatMap -> {

                    AirlineResponse airline = airlineCache.computeIfAbsent(
                            seatMap.getAirlineId(),
                            id -> {
                                try {
                                    return airlineServiceClient.getAirlineById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch airline: " + id, e);
                                }
                            }
                    );

                    AircraftResponse aircraft = aircraftCache.computeIfAbsent(
                            seatMap.getAircraftId(),
                            id -> {
                                try {
                                    return airlineServiceClient.getAircraftById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch aircraft: " + id, e);
                                }
                            }
                    );

                    return toMapResponse(seatMap, airline, aircraft);
                })
                .toList();
    }

}
