package com.vaishnav.airlinq.seat.controller;

import com.vaishnav.airlinq.seat.service.SeatMapService;
import com.vaishnav.payload.request.SeatMapRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.SeatMapResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-maps")
@RequiredArgsConstructor
public class SeatMapController {

    private final SeatMapService seatMapService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatMapResponse createSeatMap(
            @Valid @RequestBody SeatMapRequest request,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return seatMapService.createSeatMap(userId, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeatMapResponse getSeatMapById(@PathVariable Long id) throws Exception {
        return seatMapService.getSeatMapById(id);
    }

    @GetMapping("/airline")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatMapResponse> getSeatMapsByAirline(
            @RequestHeader("X-User-Id") Long airlineId
    ) {
        return seatMapService.getSeatMapsByAirlineId(airlineId);
    }

    @GetMapping("/aircraft/{aircraftId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatMapResponse> getSeatMapsByAircraft(@PathVariable Long aircraftId) {
        return seatMapService.getSeatMapsByAircraftId(aircraftId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeatMapResponse updateSeatMap(
            @PathVariable Long id,
            @RequestBody SeatMapRequest request
    ) throws Exception {
        return seatMapService.updateSeatMap(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSeatMap(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        seatMapService.deleteSeatMap(userId, id);
        return ResponseEntity.ok(new ApiResponse("Seat map deleted successfully", true));
    }

}
