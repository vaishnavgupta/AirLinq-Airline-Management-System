package com.vaishnav.airlinq.seat.service.impl;

import com.vaishnav.airlinq.seat.client.AirlineServiceClient;
import com.vaishnav.airlinq.seat.client.FlightOpsServiceClient;
import com.vaishnav.airlinq.seat.mapper.SeatInstanceMapper;
import com.vaishnav.airlinq.seat.model.FlightInstanceCabin;
import com.vaishnav.airlinq.seat.model.Seat;
import com.vaishnav.airlinq.seat.model.SeatInstance;
import com.vaishnav.airlinq.seat.model.SeatMap;
import com.vaishnav.airlinq.seat.repository.FlightInstanceCabinRepository;
import com.vaishnav.airlinq.seat.repository.SeatInstanceRepository;
import com.vaishnav.airlinq.seat.repository.SeatMapRepository;
import com.vaishnav.airlinq.seat.repository.SeatRepository;
import com.vaishnav.airlinq.seat.service.SeatInstanceService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatInstanceStatus;
import com.vaishnav.payload.request.GenerateSeatInstanceRequest;
import com.vaishnav.payload.request.SeatBookRequest;
import com.vaishnav.payload.request.SeatHoldRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import com.vaishnav.payload.response.SeatInstanceResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatInstanceServiceImpl implements SeatInstanceService {
    private final SeatMapRepository seatMapRepository;
    private final SeatInstanceRepository seatInstanceRepository;
    private final SeatRepository seatRepository;
    private final FlightInstanceCabinRepository flightInstanceCabinRepository;
    private final AirlineServiceClient airlineServiceClient;
    private final FlightOpsServiceClient flightOpsServiceClient;

    @Override
    @Transactional
    public List<SeatInstanceResponse> generateSeatInstances(
            Long userId,
            GenerateSeatInstanceRequest request
    ) throws Exception {
        AirlineResponse airlineResponse = airlineServiceClient.getAirlineByUserId(userId);

        if(airlineResponse == null || airlineResponse.getId()==null){
            throw new Exception("Airline not found with given id");
        }

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(request.getFlightInstanceId());

        if(flightInstanceResponse == null || flightInstanceResponse.getId()==null){
            throw new Exception("Flight instance not found with given id");
        }

        AircraftResponse aircraftResponse = airlineServiceClient.getAircraftById(flightInstanceResponse.getAircraftId());

        if(aircraftResponse == null || aircraftResponse.getId()==null){
            throw new Exception("Aircraft not found with given id");
        }


        if(!flightInstanceResponse.getAirlineId().equals(airlineResponse.getId())){
            throw new Exception("Flight Instance does not belong to Airline with given id");
        }

        if(!flightInstanceResponse.getAircraftId().equals(aircraftResponse.getId())){
            throw new Exception("Flight Instance does not belong to Aircraft with given id");
        }

        SeatMap seatMap = seatMapRepository.findFirstByAircraftIdAndIsActiveTrue(aircraftResponse.getId())
                .orElseThrow(() -> new Exception("SeatMap not found with provided id"));

        List<Seat> seats = seatRepository.findBySeatMapIdAndIsActiveTrue(seatMap.getId());

        if(seats.isEmpty()){
            throw new Exception("No active seats found for the seat map");
        }

        boolean isGenerated = seatInstanceRepository.existsByFlightInstanceId(
                flightInstanceResponse.getId()
        );

        if(isGenerated){
            throw new Exception(
                    "Seat instances already generated for flight instance id: "
                            + flightInstanceResponse.getId()
            );
        }

        // 1. Creating FlightInstanceCabin Objects
        Map<CabinClass, List<Seat>> seatsByCabinClass = seats
                .stream()
                .collect(Collectors.groupingBy(Seat::getCabinClass));

        Map<CabinClass, FlightInstanceCabin> cabinByCabinClass = new HashMap<>();

        for(Map.Entry<CabinClass, List<Seat>> entry : seatsByCabinClass.entrySet()){
            CabinClass cabinClass = entry.getKey();
            List<Seat> seatList = entry.getValue();

            FlightInstanceCabin flightInstanceCabin = FlightInstanceCabin.builder()
                    .airlineId(airlineResponse.getId())
                    .flightInstanceId(flightInstanceResponse.getId())
                    .cabinClass(cabinClass)
                    .totalSeats(seatList.size())
                    .availableSeats(seatList.size())
                    .bookedSeats(0)
                    .blockedSeats(0)
                    .isActive(true)
                    .build();

            flightInstanceCabin = flightInstanceCabinRepository.save(flightInstanceCabin);
            cabinByCabinClass.put(cabinClass, flightInstanceCabin);
        }

        // 2. Creating SeatInstance Objects
        List<SeatInstance> seatInstances = seats.stream()
                .map(seat -> {
                    FlightInstanceCabin cabin = cabinByCabinClass.get(seat.getCabinClass());

                    return SeatInstance.builder()
                            .airlineId(airlineResponse.getId())
                            .flightInstanceId(flightInstanceResponse.getId())
                            .seatId(seat.getId())
                            .flightInstanceCabinId(cabin.getId())
                            .seatNumber(seat.getSeatNumber())
                            .rowNumber(seat.getRowNumber())
                            .seatColumn(seat.getSeatColumn())
                            .cabinClass(cabin.getCabinClass())
                            .seatType(seat.getSeatType())
                            .status(SeatInstanceStatus.AVAILABLE)
                            .bookingId(null)
                            .passengerId(null)
                            .seatFee(null)
                            .isExitRow(seat.getIsExitRow())
                            .hasExtraLegroom(seat.getHasExtraLegroom())
                            .lockedUntil(null)
                            .build();
                })
                .toList();

        return seatInstanceRepository.saveAll(seatInstances)
                .stream()
                .map(seat -> toResponse(seat, flightInstanceResponse))
                .toList();

    }

    @Override
    public SeatInstanceResponse getSeatInstanceById(Long id) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat Instance does not exists with id"));
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(seatInstance.getFlightInstanceId());
        return toResponse(seatInstance, flightInstanceResponse);
    }

    @Override
    public List<SeatInstanceResponse> getSeatsByFlightInstanceId(Long flightInstanceId) throws Exception {
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(flightInstanceId);

        if(flightInstanceResponse == null || flightInstanceResponse.getId() == null){
            throw new Exception("Flight Instance does not exists");
        }

        return seatInstanceRepository.findByFlightInstanceId(flightInstanceId)
                .stream()
                .map(seat -> toResponse(seat, flightInstanceResponse))
                .toList();
    }

    @Override
    public List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceId(Long flightInstanceId) throws Exception {
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(flightInstanceId);

        if(flightInstanceResponse == null || flightInstanceResponse.getId() == null){
            throw new Exception("Flight Instance does not exists");
        }

        return seatInstanceRepository.findByFlightInstanceIdAndStatus(
                flightInstanceId,
                SeatInstanceStatus.AVAILABLE
        )
                .stream()
                .map(seat -> toResponse(seat, flightInstanceResponse))
                .toList();
    }

    @Override
    public List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceIdAndCabinClass(Long flightInstanceId, CabinClass cabinClass) throws Exception {
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstance(flightInstanceId);

        if(flightInstanceResponse == null || flightInstanceResponse.getId() == null){
            throw new Exception("Flight Instance does not exists");
        }

        return seatInstanceRepository.findByFlightInstanceIdAndCabinClassAndStatus(
                flightInstanceId,
                cabinClass,
                SeatInstanceStatus.AVAILABLE
        )
                .stream()
                .map(seat -> toResponse(seat, flightInstanceResponse))
                .toList();
    }

    @Override
    @Transactional
    public SeatInstanceResponse holdSeat(Long seatInstanceId, SeatHoldRequest request) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElseThrow(() -> new Exception("Seat Instance does not exists with id"));

        if(!seatInstance.getStatus().equals(SeatInstanceStatus.AVAILABLE)){
            throw new Exception("Seat Instance is not available for hold");
        }

        int holdMinutes = request.getHoldMinutes() != null ? request.getHoldMinutes() : 10;

        seatInstance.setStatus(SeatInstanceStatus.HELD);
        seatInstance.setBookingId(request.getBookingId());
        seatInstance.setPassengerId(request.getPassengerId());
        seatInstance.setLockedUntil(Instant.now().plus(holdMinutes, ChronoUnit.MINUTES));

        seatInstance = seatInstanceRepository.save(seatInstance);

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient
                .getFlightInstance(seatInstance.getFlightInstanceId());

        return toResponse(seatInstance, flightInstanceResponse);
    }

    @Override
    @Transactional
    public SeatInstanceResponse bookSeat(Long seatInstanceId, SeatBookRequest request) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElseThrow(() -> new Exception("Seat Instance does not exists with id"));

        if (seatInstance.getStatus() == SeatInstanceStatus.BOOKED) {
            throw new Exception("Seat is already booked");
        }

        if (seatInstance.getStatus() == SeatInstanceStatus.BLOCKED) {
            throw new Exception("Seat is blocked and cannot be booked");
        }

        if (seatInstance.getStatus() == SeatInstanceStatus.HELD) {
            validateHeldSeatOwnership(seatInstance, request.getBookingId());
            validateHeldSeatNotExpired(seatInstance);
        }
        FlightInstanceCabin cabin = getCabinForSeatInstance(seatInstance);

        if(cabin.getAvailableSeats() <= 0) {
            throw new Exception("No available seats left in cabin: ");
        }

        seatInstance.setStatus(SeatInstanceStatus.BOOKED);
        seatInstance.setBookingId(request.getBookingId());
        seatInstance.setPassengerId(request.getPassengerId());
        seatInstance.setLockedUntil(null);

        cabin.setAvailableSeats(cabin.getAvailableSeats() - 1);
        cabin.setBookedSeats(cabin.getBookedSeats() + 1);

        flightInstanceCabinRepository.save(cabin);
        seatInstance = seatInstanceRepository.save(seatInstance);

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient
                .getFlightInstance(seatInstance.getFlightInstanceId());

        return toResponse(seatInstance, flightInstanceResponse);
    }


    @Override
    @Transactional
    public SeatInstanceResponse releaseSeat(Long seatInstanceId) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElseThrow(() -> new Exception(
                        "Seat instance not found with id: " + seatInstanceId
                ));

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient
                .getFlightInstance(seatInstance.getFlightInstanceId());

        SeatInstanceStatus status = seatInstance.getStatus();

        if (status == SeatInstanceStatus.AVAILABLE) {
            return toResponse(seatInstance, flightInstanceResponse);
        }

        if(status == SeatInstanceStatus.BLOCKED) {
            throw new Exception("Seat is blocked cannot be released");
        }

        if(status == SeatInstanceStatus.BOOKED) {
            FlightInstanceCabin cabin = getCabinForSeatInstance(seatInstance);
            cabin.setAvailableSeats(cabin.getAvailableSeats() + 1);
            cabin.setBookedSeats(Math.max(0, cabin.getBookedSeats() - 1));

            flightInstanceCabinRepository.save(cabin);
        }

        seatInstance.setStatus(SeatInstanceStatus.AVAILABLE);
        seatInstance.setBookingId(null);
        seatInstance.setPassengerId(null);
        seatInstance.setLockedUntil(null);

        seatInstance = seatInstanceRepository.save(seatInstance);

        return toResponse(seatInstance, flightInstanceResponse);
    }

    @Override
    @Transactional
    public SeatInstanceResponse blockSeat(Long seatInstanceId) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElseThrow(() -> new Exception(
                        "Seat instance not found with id: " + seatInstanceId
                ));

        if(seatInstance.getStatus() != SeatInstanceStatus.AVAILABLE) {
            throw new Exception("Only available seat instance can be blocked");
        }

        FlightInstanceCabin cabin = getCabinForSeatInstance(seatInstance);

        if(cabin.getAvailableSeats() <= 0) {
            throw new Exception("No available seats left in cabin: ");
        }

        seatInstance.setStatus(SeatInstanceStatus.BLOCKED);
        seatInstance.setBookingId(null);
        seatInstance.setPassengerId(null);
        seatInstance.setLockedUntil(null);

        cabin.setAvailableSeats(cabin.getAvailableSeats() - 1);
        cabin.setBlockedSeats(cabin.getBlockedSeats() + 1);

        flightInstanceCabinRepository.save(cabin);
        seatInstance = seatInstanceRepository.save(seatInstance);

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient
                .getFlightInstance(seatInstance.getFlightInstanceId());

        return toResponse(seatInstance, flightInstanceResponse);
    }

    @Override
    @Transactional
    public SeatInstanceResponse unblockSeat(Long seatInstanceId) throws Exception {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElseThrow(() -> new Exception(
                        "Seat instance not found with id: " + seatInstanceId
                ));

        if(seatInstance.getStatus() != SeatInstanceStatus.BLOCKED) {
            throw new Exception("Only blocked seat instance can be unblocked");
        }

        FlightInstanceCabin cabin =  getCabinForSeatInstance(seatInstance);

        seatInstance.setStatus(SeatInstanceStatus.AVAILABLE);
        seatInstance.setBookingId(null);
        seatInstance.setPassengerId(null);
        seatInstance.setLockedUntil(null);

        cabin.setAvailableSeats(cabin.getAvailableSeats() + 1);
        cabin.setBlockedSeats(Math.max(0, cabin.getBlockedSeats() - 1));

        flightInstanceCabinRepository.save(cabin);
        seatInstance = seatInstanceRepository.save(seatInstance);

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient
                .getFlightInstance(seatInstance.getFlightInstanceId());

        return toResponse(seatInstance, flightInstanceResponse);
    }

    @Override
    @Transactional
    public void releaseExpiredHeldSeats() {
        List<SeatInstance> expiredSeats = seatInstanceRepository.findByStatusAndLockedUntilBefore(
                SeatInstanceStatus.HELD,
                Instant.now()
        );
        for (SeatInstance seatInstance : expiredSeats) {
            seatInstance.setStatus(SeatInstanceStatus.AVAILABLE);
            seatInstance.setBookingId(null);
            seatInstance.setPassengerId(null);
            seatInstance.setLockedUntil(null);
        }

        seatInstanceRepository.saveAll(expiredSeats);
    }

    private SeatInstanceResponse toResponse(SeatInstance seatInstance, FlightInstanceResponse flightInstanceResponse) {
        return SeatInstanceMapper.toResponse(seatInstance, flightInstanceResponse);
    }

    private void validateHeldSeatOwnership(
            SeatInstance seatInstance,
            Long bookingId
    ) throws Exception {
        if (seatInstance.getBookingId() == null || !seatInstance.getBookingId().equals(bookingId)) {
            throw new Exception("Seat is held by another booking");
        }
    }

    private void validateHeldSeatNotExpired(SeatInstance seatInstance) throws Exception {
        if (seatInstance.getLockedUntil() != null &&
                seatInstance.getLockedUntil().isBefore(Instant.now())) {
            throw new Exception("Seat hold has expired");
        }
    }

    private FlightInstanceCabin getCabinForSeatInstance(SeatInstance seatInstance) throws Exception {
        return flightInstanceCabinRepository.findByFlightInstanceIdAndCabinClassAndIsActiveTrue(
                        seatInstance.getFlightInstanceId(),
                        seatInstance.getCabinClass()
                )
                .orElseThrow(() -> new Exception(
                        "Flight instance cabin not found for flightInstanceId: "
                                + seatInstance.getFlightInstanceId()
                                + " and cabinClass: "
                                + seatInstance.getCabinClass()
                ));
    }

}
