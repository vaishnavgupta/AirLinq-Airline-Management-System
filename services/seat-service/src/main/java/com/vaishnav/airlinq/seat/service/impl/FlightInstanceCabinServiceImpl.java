package com.vaishnav.airlinq.seat.service.impl;

import com.vaishnav.airlinq.seat.mapper.FlightInstanceCabinMapper;
import com.vaishnav.airlinq.seat.model.FlightInstanceCabin;
import com.vaishnav.airlinq.seat.repository.FlightInstanceCabinRepository;
import com.vaishnav.airlinq.seat.service.FlightInstanceCabinService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.FlightInstanceCabinResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInstanceCabinServiceImpl implements FlightInstanceCabinService {

    private final FlightInstanceCabinRepository flightInstanceCabinRepository;

    @Override
    public List<FlightInstanceCabinResponse> getCabinsByFlightInstanceId(Long flightInstanceId) {
        return flightInstanceCabinRepository.findByFlightInstanceIdAndIsActiveTrue(flightInstanceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FlightInstanceCabinResponse getCabinByFlightInstanceIdAndCabinClass(Long flightInstanceId, CabinClass cabinClass) throws Exception {
        FlightInstanceCabin flightInstanceCabin = flightInstanceCabinRepository.findByFlightInstanceIdAndCabinClassAndIsActiveTrue(
                flightInstanceId, cabinClass
        ).orElseThrow(() -> new Exception("FlightInstanceCabin not found"));
        return toResponse(flightInstanceCabin);
    }

    @Override
    public void increaseBookedSeats(Long flightInstanceId, CabinClass cabinClass) throws Exception {
        FlightInstanceCabin flightInstanceCabin = flightInstanceCabinRepository.findByFlightInstanceIdAndCabinClassAndIsActiveTrue(
                flightInstanceId, cabinClass
        ).orElseThrow(() -> new Exception("FlightInstanceCabin not found"));

        if(flightInstanceCabin.getAvailableSeats() < 1) {
            throw new Exception("No AvailableSeats");
        }
        flightInstanceCabin.setAvailableSeats(flightInstanceCabin.getAvailableSeats() - 1);
        flightInstanceCabin.setBookedSeats(flightInstanceCabin.getBookedSeats() + 1);
        flightInstanceCabinRepository.save(flightInstanceCabin);
    }

    @Override
    public void releaseBookedSeat(Long flightInstanceId, CabinClass cabinClass) throws Exception {
        FlightInstanceCabin flightInstanceCabin = flightInstanceCabinRepository.findByFlightInstanceIdAndCabinClassAndIsActiveTrue(
                flightInstanceId, cabinClass
        ).orElseThrow(() -> new Exception("FlightInstanceCabin not found"));

        if(flightInstanceCabin.getBookedSeats() < 1) {
            throw new Exception("No Booked Seats");
        }
        flightInstanceCabin.setAvailableSeats(flightInstanceCabin.getAvailableSeats() + 1);
        flightInstanceCabin.setBookedSeats(flightInstanceCabin.getBookedSeats() - 1);
        flightInstanceCabinRepository.save(flightInstanceCabin);
    }

    private FlightInstanceCabinResponse toResponse(FlightInstanceCabin flightInstanceCabin) {
        AirlineResponse airlineResponse = AirlineResponse.builder()
                .id(flightInstanceCabin.getAirlineId())
                .build();
        FlightInstanceResponse flightInstanceResponse = FlightInstanceResponse.builder()
                .id(flightInstanceCabin.getFlightInstanceId())
                .build();
        return FlightInstanceCabinMapper.toResponse(
                flightInstanceCabin, airlineResponse, flightInstanceResponse
        );
    }

}
