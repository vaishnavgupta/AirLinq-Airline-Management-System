package com.vaishnav.airlinq.seat.controller;

import com.vaishnav.airlinq.seat.service.SeatInstanceService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.GenerateSeatInstanceRequest;
import com.vaishnav.payload.request.SeatBookRequest;
import com.vaishnav.payload.request.SeatHoldRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.SeatInstanceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-instances")
@RequiredArgsConstructor
public class SeatInstanceController {

    private final SeatInstanceService seatInstanceService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SeatInstanceResponse> generateSeatInstances(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody GenerateSeatInstanceRequest request
    ) throws Exception {
        return seatInstanceService.generateSeatInstances(userId, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse getSeatInstanceById(
            @PathVariable Long id
    ) throws Exception {
        return seatInstanceService.getSeatInstanceById(id);
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatInstanceResponse> getSeatsByFlightInstanceId(
            @PathVariable Long flightInstanceId
    ) throws Exception {
        return seatInstanceService.getSeatsByFlightInstanceId(flightInstanceId);
    }

    @GetMapping("/flight-instance/{flightInstanceId}/available")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceId(
            @PathVariable Long flightInstanceId
    ) throws Exception {
        return seatInstanceService.getAvailableSeatsByFlightInstanceId(flightInstanceId);
    }

    @GetMapping("/flight-instance/{flightInstanceId}/available/cabin-class/{cabinClass}")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatInstanceResponse> getAvailableSeatsByFlightInstanceIdAndCabinClass(
            @PathVariable Long flightInstanceId,
            @PathVariable CabinClass cabinClass
    ) throws Exception {
        return seatInstanceService.getAvailableSeatsByFlightInstanceIdAndCabinClass(
                flightInstanceId,
                cabinClass
        );
    }

    @PostMapping("/{id}/hold")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse holdSeat(
            @PathVariable Long id,
            @Valid @RequestBody SeatHoldRequest request
    ) throws Exception {
        return seatInstanceService.holdSeat(id, request);
    }

    @PostMapping("/{id}/book")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse bookSeat(
            @PathVariable Long id,
            @Valid @RequestBody SeatBookRequest request
    ) throws Exception {
        return seatInstanceService.bookSeat(id, request);
    }

    @PostMapping("/{id}/release")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse releaseSeat(
            @PathVariable Long id
    ) throws Exception {
        return seatInstanceService.releaseSeat(id);
    }

    @PostMapping("/{id}/block")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse blockSeat(
            @PathVariable Long id
    ) throws Exception {
        return seatInstanceService.blockSeat(id);
    }

    @PostMapping("/{id}/unblock")
    @ResponseStatus(HttpStatus.OK)
    public SeatInstanceResponse unblockSeat(
            @PathVariable Long id
    ) throws Exception {
        return seatInstanceService.unblockSeat(id);
    }

    @PostMapping("/release-expired-held")
    public ResponseEntity<ApiResponse> releaseExpiredHeldSeats() {
        seatInstanceService.releaseExpiredHeldSeats();
        ApiResponse response = new ApiResponse("Expired held seats released successfully", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
