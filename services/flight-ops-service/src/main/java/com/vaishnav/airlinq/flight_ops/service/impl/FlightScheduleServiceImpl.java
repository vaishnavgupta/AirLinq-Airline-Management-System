package com.vaishnav.airlinq.flight_ops.service.impl;

import com.vaishnav.airlinq.flight_ops.client.AirlineServiceClient;
import com.vaishnav.airlinq.flight_ops.client.LocationServiceClient;
import com.vaishnav.airlinq.flight_ops.mapper.FlightScheduleMapper;
import com.vaishnav.airlinq.flight_ops.model.Flight;
import com.vaishnav.airlinq.flight_ops.model.FlightSchedule;
import com.vaishnav.airlinq.flight_ops.repository.FlightRepository;
import com.vaishnav.airlinq.flight_ops.repository.FlightScheduleRepository;
import com.vaishnav.airlinq.flight_ops.service.FlightInstanceService;
import com.vaishnav.airlinq.flight_ops.service.FlightScheduleService;
import com.vaishnav.enums.FlightStatus;
import com.vaishnav.payload.request.FlightInstanceRequest;
import com.vaishnav.payload.request.FlightScheduleRequest;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.FlightScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl implements FlightScheduleService {
    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightInstanceService flightInstanceService;
    private final AirlineServiceClient airlineServiceClient;
    private final LocationServiceClient locationServiceClient;

    @Override
    public FlightScheduleResponse createFlightSchedule(Long userId, FlightScheduleRequest request) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);

        if(airlineResponse == null || airlineResponse.getId() == null) {
            throw new Exception("Airline not found with owner id");
        }

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new Exception("Flight does not exists with id"));

        if (!flight.getAirlineId().equals(airlineResponse.getId())) {
            throw new Exception("Flight does not belong to this airline owner");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new Exception("Start date is after end date ");
        }

        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flight.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flight.getDepartureAirportId());

        FlightSchedule flightSchedule = FlightScheduleMapper.toFlightSchedule(request, flight);
        flightSchedule = flightScheduleRepository.save(flightSchedule);

        //Create Flight Instance for saved schedule
        List<DayOfWeek> operatingDays = flightSchedule.getOperatingDays();
        LocalDate startDate = flightSchedule.getStartDate();
        LocalDate endDate = flightSchedule.getEndDate();

        FlightInstanceRequest flightInstanceRequest = FlightInstanceRequest.builder()
                .flightId(flight.getId())
                .airlineId(airlineResponse.getId())
                .scheduleId(flightSchedule.getId())
                .departureAirportId(flight.getDepartureAirportId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .status(FlightStatus.SCHEDULED)
                .build();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (operatingDays.contains(date.getDayOfWeek())) {
                flightInstanceRequest.setDepartureDateTime(
                        LocalDateTime.of(date, flightSchedule.getDepartureTime())
                );
                flightInstanceRequest.setArrivalDateTime(
                        LocalDateTime.of(date, flightSchedule.getArrivalTime())
                );
                flightInstanceService.createFlightInstance(
                        userId, flightInstanceRequest
                );
            }
        }

        return buildResponse(flightSchedule, arrivalAirport, departureAirport);
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) throws Exception {
        FlightSchedule flightSchedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight Schedule does not exists with id"));
        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flightSchedule.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flightSchedule.getDepartureAirportId());
        return buildResponse(flightSchedule, arrivalAirport, departureAirport);
    }

    @Override
    public List<FlightScheduleResponse> getFlightScheduleByAirline(Long userId) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);

        if(airlineResponse == null || airlineResponse.getId() == null) {
            throw new Exception("Airline not found with owner id");
        }

        List<FlightSchedule> flightSchedules = flightScheduleRepository.findByFlightAirlineId(airlineResponse.getId());
        return convertListToFlightScheduleResponse(flightSchedules);
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest) throws Exception {
        FlightSchedule flightSchedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight Schedule does not exists with id"));

        if (flightScheduleRequest.getStartDate() != null &&
                flightScheduleRequest.getEndDate() != null &&
                flightScheduleRequest.getStartDate().isAfter(flightScheduleRequest.getEndDate())
        ) {
            throw new Exception("Start date is after end date ");
        }

        FlightScheduleMapper.updateEntity(flightScheduleRequest, flightSchedule);
        flightSchedule = flightScheduleRepository.save(flightSchedule);

        AirportResponse arrivalAirport = locationServiceClient.getAirportById(flightSchedule.getArrivalAirportId());
        AirportResponse departureAirport = locationServiceClient.getAirportById(flightSchedule.getDepartureAirportId());

        return buildResponse(flightSchedule,arrivalAirport,departureAirport);
    }

    @Override
    public void deleteFlightSchedule(Long id) throws Exception {
        FlightSchedule flightSchedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight Schedule does not exists with id"));

        flightScheduleRepository.delete(flightSchedule);
    }

    private FlightScheduleResponse buildResponse(
            FlightSchedule fs,
            AirportResponse arrivalAirport,
            AirportResponse departureAirport
    ) {
        return FlightScheduleMapper.toFlightScheduleResponse(
                fs, departureAirport, arrivalAirport
        );
    }

    private List<FlightScheduleResponse> convertListToFlightScheduleResponse(
            List<FlightSchedule> schedules
    ) {

        Map<Long, AirportResponse> airportCache = new HashMap<>();

        return schedules.stream()
                .map(fs -> {
                    AirportResponse departure = airportCache.computeIfAbsent(
                            fs.getDepartureAirportId(),
                            locationServiceClient::getAirportById
                    );
                    AirportResponse arrival = airportCache.computeIfAbsent(
                            fs.getArrivalAirportId(),
                            locationServiceClient::getAirportById
                    );
                    return buildResponse(
                            fs,
                            arrival,
                            departure
                    );
                })
                .toList();
    }

}
